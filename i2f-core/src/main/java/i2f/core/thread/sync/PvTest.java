package i2f.core.thread.sync;

import java.security.SecureRandom;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author Ice2Faith
 * @date 2023/5/6 17:56
 * @desc
 */
public class PvTest {
    public static void main(String[] args) throws Exception {
//        testNormal();
        testLink();
    }

    public static void testLink() throws Exception {
        ProcessTaskRunner runner = new ProcessTaskRunner(ForkJoinPool.commonPool());

        SecureRandom rand = new SecureRandom();

        Callable<?>[] arr = new Callable<?>[5];
        for (int i = 0; i < 5; i++) {
            final String iter = i + "";
            arr[i] = () -> {
                System.out.println(iter);
                Thread.sleep(rand.nextInt(30));
                return iter;
            };
        }

        runner.addLinkTask(arr[0], arr[1]);
        runner.addLinkTask(arr[1], arr[2]);
        runner.addLinkTask(arr[1], arr[3]);
        runner.addLinkTask(arr[2], arr[4]);
        runner.addLinkTask(arr[3], arr[4]);

        Map<String, Optional<?>> ret = runner.call();
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        System.out.println(ret);
    }

    public static void testNormal() throws Exception {
        ProcessTaskRunner runner = new ProcessTaskRunner(ForkJoinPool.commonPool());

        SecureRandom rand = new SecureRandom();
        runner.addTask("1", () -> {
            System.out.println("1");
            Thread.sleep(rand.nextInt(30));
            return "1";
        });

        runner.addTask("2", () -> {
            System.out.println("2");
            Thread.sleep(rand.nextInt(30));
            return "2";
        });

        runner.addTask("3", () -> {
            System.out.println("3");
            Thread.sleep(rand.nextInt(30));
            return "3";
        });

        runner.addTask("4", () -> {
            System.out.println("4");
            Thread.sleep(rand.nextInt(30));
            return "4";
        });

        runner.addTask("5", () -> {
            System.out.println("5");
            Thread.sleep(rand.nextInt(30));
            return "5";
        });

        runner.addLink("1", "2");
        runner.addLink("2", "3");
        runner.addLink("2", "4");
        runner.addLink("3", "5");
        runner.addLink("4", "5");

        Map<String, Optional<?>> ret = runner.call();
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        System.out.println(ret);

    }
}
