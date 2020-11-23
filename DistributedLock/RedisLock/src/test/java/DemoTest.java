import com.vino.RedisLockApplication;
import com.vino.TestRunnable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest(classes = RedisLockApplication.class)
@RunWith(SpringRunner.class)
public class DemoTest {

    @Test
    public void lockTest() {
        ExecutorService eService = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            eService.execute(new TestRunnable());
        }
        // 待线程都执行完毕之后再关闭, 但是如果调用的是shutdownNow() 方法，则相当于调用每个线程的 interrupt() 方法
        eService.shutdown();
    }
}
