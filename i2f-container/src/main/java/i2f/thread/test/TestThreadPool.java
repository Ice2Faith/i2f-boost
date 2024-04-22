package i2f.thread.test;

import i2f.thread.DynamicThreadPool;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/4/12 17:12
 * @desc
 */
public class TestThreadPool {
    public static void main(String[] args) throws InterruptedException {

        DynamicThreadPool pool = new DynamicThreadPool();

        SecureRandom random = new SecureRandom();

        int count = 5000;
//        int[] sleepArr={0,0,0,30,300,1000,2000,3000,5000,10000};
        int[] sleepArr = {0, 0, 0, 30, 3000, 300, 1000, 10000, 2000, 5000};
        long[] execArr = new long[sleepArr.length];
        for (int t = 0; t < sleepArr.length; t++) {

            boolean isFirst = t == 0;
            long tbs = System.currentTimeMillis();
            int currSleepTs = sleepArr[t];
            CountDownLatch latch = new CountDownLatch(count);
            System.out.println("sleep:" + currSleepTs);
            new Thread(() -> {
                try {
                    Thread.sleep(isFirst ? 8000 : 300);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                for (int i = 0; i < count; i++) {
                    int v = i;
                    try {
                        pool.submit(() -> {
                            try {
//                            System.out.println("begin ... "+ v);
                                if (currSleepTs > 0) {
                                    Thread.sleep(random.nextInt(currSleepTs));
                                }
                                int cnt = random.nextInt(10000);
                                double sum = 0;
                                for (int j = 0; j < cnt; j++) {
                                    double p = Math.sin(j);
                                    p = Math.sqrt(p);
                                    p = Math.pow(p, j);
                                    sum += p;
                                }
//                            System.out.println("end ... "+v+",sum="+sum);
                                return sum;
                            } finally {
                                latch.countDown();
                            }
                        });
                        Thread.sleep(random.nextInt(5));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            latch.await();

            System.out.println("ok");

            long tes = System.currentTimeMillis();
            long diff = tes - tbs;
            execArr[t] = diff;
        }

        for (int i = 0; i < sleepArr.length; i++) {
            System.out.println(sleepArr[i] + ":" + execArr[i]);
        }
    }
}
