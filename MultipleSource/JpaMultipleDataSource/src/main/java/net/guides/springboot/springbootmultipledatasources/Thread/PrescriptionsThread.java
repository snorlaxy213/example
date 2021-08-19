package net.guides.springboot.springbootmultipledatasources.Thread;

import net.guides.springboot.springbootmultipledatasources.security.entities.User;
import net.guides.springboot.springbootmultipledatasources.security.repositories.UserRepository;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class PrescriptionsThread implements Runnable {

    UserRepository userRepository;
    private List<User> list;
    private CountDownLatch begin;
    private CountDownLatch end;
    public Integer count = 0;

    public PrescriptionsThread() {
    }

    /**
     * @param list 入库数据
     * @param begin 计时器
     * @param end   计时器
     * @param userRepository  数据库连接
     */
    public PrescriptionsThread(List<User> list, CountDownLatch begin, CountDownLatch end, UserRepository userRepository) {
        this.list = list;
        this.begin = begin;
        this.end = end;
        this.userRepository = userRepository;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public void run() {

        try {
            for (User user :list) {
                this.userRepository.save(user);
            }
            count = 1;
            begin.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            end.countDown();
        }
    }
}
