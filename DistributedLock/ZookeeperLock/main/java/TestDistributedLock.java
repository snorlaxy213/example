import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.CountDownLatch;

public class TestDistributedLock {

    public static void main(String[] args) throws Exception {
//        //模拟10个线程去访问锁，只有一个线程获得锁。
//        CountDownLatch countDownLatch=new CountDownLatch(10);
//        for (int i = 0; i <10; i++) {
//            int k = i;
//
//            new Thread(()->{
//                try {
//                    //先阻塞
//                    countDownLatch.await();
//                    DistributeLock distributeLock=new DistributeLock();
//                    distributeLock.lock();//获得锁
//
//                    Thread.sleep(k * 1000);
//                    distributeLock.unlock();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//                    ,"Thread-"+i).start();
//            countDownLatch.countDown();
//        }
//        //键盘输入。
//        System.in.read();

        sharedReentrantLock();
    }

    /**
     * InterProcessMutex
     * 此锁可以重入，但是重入几次需要释放几次。
     *
     * @throws Exception
     */
    public static void sharedReentrantLock() throws Exception {

        // ZooKeeper 锁节点路径, 分布式锁的相关操作都是在这个节点上进行
         final String lockPath = "/distributed-lock";

        // ZooKeeper 服务地址, 单机格式为:(127.0.0.1:2181),
        // 集群格式为:(127.0.0.1:2181,127.0.0.1:2182,127.0.0.1:2183)
         String connectString = "1.14.163.139:2181";

        // Curator 客户端重试策略
         RetryPolicy retry = new ExponentialBackoffRetry(1000, 3);

         CuratorFramework client = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);

        // client2 用户模拟其他客户端
         CuratorFramework client2 = CuratorFrameworkFactory.newClient(connectString, 60000, 15000, retry);

        // 创建共享锁
        final InterProcessLock lock = new InterProcessMutex(client, lockPath);
        // lock2 用于模拟其他客户端
        final InterProcessLock lock2 = new InterProcessMutex(client2, lockPath);

        final CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    lock.acquire();
                    System.out.println("1获取锁===============");
                    // 测试锁重入
                    lock.acquire();
                    System.out.println("1再次获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock.release();
                    System.out.println("1释放锁===============");
                    lock.release();
                    System.out.println("1再次释放锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 获取锁对象
                try {
                    lock2.acquire();
                    System.out.println("2获取锁===============");
                    // 测试锁重入
                    lock2.acquire();
                    System.out.println("2再次获取锁===============");
                    Thread.sleep(5 * 1000);
                    lock2.release();
                    System.out.println("2释放锁===============");
                    lock2.release();
                    System.out.println("2再次释放锁===============");

                    countDownLatch.countDown();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        countDownLatch.await();
    }
}
