package milliondata.service.impl;

import com.alibaba.fastjson.JSON;
import milliondata.enums.DateTimeFormat;
import milliondata.model.ErrorInfo;
import milliondata.model.User;
import milliondata.service.UserService;
import milliondata.service.thread.UserThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    
    @Override
    public Integer addUser(User user) {
        return 1;
    }
    
    /**
     * 导入用户数据
     * 该方法通过多线程并行处理，导入用户数据，并收集导入过程中的错误信息
     *
     * @param users 待导入的用户列表
     * @return 导入结果的Map对象，包含状态码、消息和错误信息列表
     * @throws ExecutionException   如果线程池执行过程中发生异常
     * @throws InterruptedException 如果当前线程在等待时被中断
     */
    @Override
    public Map<String, Object> importData(List<User> users) throws ExecutionException, InterruptedException {
        // 记录开始时间
        Date startTime = new Date();
        // 格式化日期
        SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeFormat.YYYY_MM_DD_HH_MM_SS.getDateTimeFormat());
        // 日志记录开始导入数据的时间
        LOGGER.info("{}，开始导入数据...", dateFormat.format(startTime));
        
        // 用于控制同时运行的线程数
        Semaphore semaphore = new Semaphore(10);
        // 初始化返回的结果Map
        Map<String, Object> result = new HashMap<>();
        // 存储导入过程中出现的错误信息
        List<ErrorInfo> errors = Collections.synchronizedList(new ArrayList<>());
        // 用户数据总行数
        int totalUsers = users.size();
        // 计算线程数量
        int totalThreads = (int) Math.ceil((double) totalUsers / 200);
        // 计数器，用于线程间同步计数
        CountDownLatch countDownLatch = new CountDownLatch(totalThreads);
        // 提取所有用户名，用于检查用户名唯一性
        Set<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toSet());
        // 创建固定数量的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(totalThreads);
        // 日志记录创建线程的信息，包括总行数和线程数量
        LOGGER.info("开始创建线程,数据总行数:{},线程数量:{}", totalUsers, totalThreads);
        // 存储所有线程的执行结果
        List<Future<Integer>> futures = new ArrayList<>();
        // 成功导入的用户数量
        int successCount = 0;
        
        // 遍历并提交所有线程任务
        for (int i = 1; i <= totalThreads; i++) {
            // 计算当前线程处理的数据范围
            int startRow = (i - 1) * 200 + 1;
            int endRow = i * 200;
            if (i == totalThreads) {
                endRow = totalUsers;
            }
            // 提交线程任务并获取Future对象
            Future<Integer> future = executorService.submit(new UserThread(semaphore, startRow, endRow, errors, usernames, users, this, countDownLatch));
            // 存储Future对象以便后续收集结果
            futures.add(future);
        }
        
        try {
            // 等待所有线程任务完成
            countDownLatch.await(60, TimeUnit.SECONDS);
            
            // 收集所有线程任务的执行结果
            for (Future<Integer> future : futures) {
                successCount += future.get();
            }
        } finally {
            // 确保所有任务完成后再关闭线程池
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        }
        
        // 记录结束时间，计算耗时
        Date endTime = new Date();
        long elapsedTime = endTime.getTime() - startTime.getTime();
        // 日志记录导入结果，包括错误信息和耗时
        LOGGER.info("执行完成,错误信息:{}", JSON.toJSONString(errors));
        LOGGER.info("{},结束导入,共{}条数据，导入成功:{}，耗时={}", dateFormat.format(endTime), totalUsers, successCount, elapsedTime);
        
        // 准备返回的结果Map
        result.put("code", 200);
        result.put("msg", "结束导入,共" + totalUsers + "条数据，导入成功" + successCount + "条，耗时:" + elapsedTime);
        result.put("data", errors);
        return result;
    }
}
