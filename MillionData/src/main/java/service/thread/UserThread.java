package service.thread;

import model.ErrorInfo;
import model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;

/**
 *
 * 插入用户多线程
 * @author Chen Jiaying
 * @date 2021/10/19
 **/

public class UserThread implements Callable<Integer> {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserThread.class);

    private Integer startRow;

    private Integer endRow;

    private List<ErrorInfo> errorInfoList;

    private Set<String> names;

    private UserService userService;

    private Semaphore semaphore;

    private CountDownLatch latch;

    private List<User> users;

    public UserThread(Semaphore semaphore, Integer startRow, Integer endRow, List<ErrorInfo> errorInfoList, Set<String> names, List<User> users, UserService userService, CountDownLatch latch) {
        this.startRow = startRow;
        this.endRow = endRow;
        this.errorInfoList = errorInfoList;
        this.users = users;
        this.names = names;
        this.userService = userService;
        this.semaphore = semaphore;
        this.latch = latch;
    }

    @Override
    public Integer call() throws Exception {
        LOGGER.info("线程ID：<{}>开始运行,startRow:{},endRow:{}", Thread.currentThread().getId(), startRow, endRow);
        semaphore.acquire();
        LOGGER.info("消耗了一个信号量，剩余信号量为:{}", semaphore.availablePermits());
        latch.countDown();
        int count = 0;
        for (int i = startRow; i <= endRow; i++) {
            User user = users.get(i);
//            count += userService.addUser(user);
            count += 1;
        }
        semaphore.release();
        return count;
    }
}
