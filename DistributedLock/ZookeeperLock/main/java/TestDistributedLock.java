import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class TestDistributedLock {

    public static void main(String[] args) throws IOException {
        //模拟10个线程去访问锁，只有一个线程获得锁。
        CountDownLatch countDownLatch=new CountDownLatch(10);
        for (int i = 0; i <10; i++) {
            int k = i;

            new Thread(()->{
                try {
                    //先阻塞
                    countDownLatch.await();
                    DistributeLock distributeLock=new DistributeLock();
                    distributeLock.lock();//获得锁

                    Thread.sleep(k * 1000);
                    distributeLock.unlock();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                    ,"Thread-"+i).start();
            countDownLatch.countDown();
        }
        //键盘输入。
        System.in.read();

    }
}
