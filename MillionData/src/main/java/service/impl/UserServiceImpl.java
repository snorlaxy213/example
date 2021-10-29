package service.impl;

import com.alibaba.fastjson.JSON;
import enums.DateTimeFormat;
import model.ErrorInfo;
import model.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import service.thread.UserThread;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * 用户管理实现类
 *
 * @author Chen Jiaying
 **/
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * 多线程导入
     * 难度：百万级别的Excel比较大，怎么传输
     *
     * @param users 用户数据
     */
    @Override
    public Map<String, Object> importData(List<User> users) throws ExecutionException, InterruptedException {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DateTimeFormat.YYYY_MM_DD_HH_MM_SS.getDateTimeFormat());
        LOGGER.info("{},开始导入数据...", format.format(now));
        //设置一个信号量为5的信号量，限制同时运行的线程数量最大为5
        Semaphore semaphore = new Semaphore(10);

        Map<String, Object> map = new HashMap<>();
        //多线程编程需要一个线程安全的ArrayList
        List<ErrorInfo> list = Collections.synchronizedList(new ArrayList<>());

        //插入User的行数
        int rows = users.size();
        //线程数量: 一个线程让他处理200个row,也许可以处理更多吧
        int threadNum = rows / 200 + 1;

        //设置一个倒计时门闩，用来处理主线程等待蚂蚁线程执行完成工作之后再运行
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        //查询是否重名 todo 千万级别的数据用户要怎么做
        Set<String> names = users.stream().map(User::getUsername).collect(Collectors.toSet());
        //创建一个定长的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);

        LOGGER.info("开始创建线程,数据总行数:{},线程数量:{}", rows, threadNum);

        List<Future<Integer>> futures = new ArrayList<>();
        int successCount = 0;

        for (int i = 1; i <= threadNum; i++) {

            int startRow = (i - 1) * 200 + 1;
            int endRow = i * 200;
            if (i == threadNum) {
                endRow = rows;
            }
            LOGGER.info("开始执行线程方法,线程ID:<{}>,线程名称:<{}>", Thread.currentThread().getId(), Thread.currentThread().getName());
            Future<Integer> future = executorService.submit(new UserThread(semaphore, startRow, endRow, list, names, users ,this, countDownLatch));
            futures.add(future);
            LOGGER.info("结束线程执行方法,返回结果:<{}>,当前线程ID:<{}>,当前线程名称:<{}>", JSON.toJSONString(future), Thread.currentThread().getId(), Thread.currentThread().getName());
            //get方法中可以设置超时时间，即规定时间内没有返回结果，则继续运行
            //get方法是线程阻塞的，调用get方法会导致后续线程因主线程阻塞而没有创建，达不到效果。
            //successCount += future.get();
        }
        //主线程等待子线程完成任务,60秒还没执行完成就继续执行

        for (Future<Integer> future : futures) {
            successCount += future.get();
        }
        //主线程等待子线程全部跑完才继续运行。设置60秒等待时间，超时后继续执行。
        countDownLatch.await(60, TimeUnit.SECONDS);
        executorService.shutdown();

        Date endDate = new Date();
        long difference = endDate.getTime() - now.getTime();
        LOGGER.info("执行完成,错误信息:{}", JSON.toJSONString(list));
        LOGGER.info("{},结束导入,共{}条数据，导入成功:{}，耗时={}", format.format(endDate), rows, successCount, difference);
        map.put("code", 200);
        map.put("msg", "结束导入,共" + rows + "条数据，导入成功" + successCount + "条，耗时:" + difference);
        map.put("data", list);
        return map;
    }

}
