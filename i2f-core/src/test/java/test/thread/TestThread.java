package test.thread;

import i2f.core.thread.AsyncMessageAwaiter;

import java.security.SecureRandom;

/**
 * @author Ice2Faith
 * @date 2023/1/29 11:17
 * @desc
 */
public class TestThread {
    public static void main(String[] args) throws InterruptedException {
        AsyncMessageAwaiter awaiter = new AsyncMessageAwaiter();
        SecureRandom random = new SecureRandom();
        for (int i = 0; i < 20; i++) {
            int iter = i;
            AsyncMessageAwaiter.Awaiter lock = awaiter.async();
            new Thread(() -> {
                int st = random.nextInt(2500) + 500;
                try {
                    Thread.sleep(st);
                } catch (Exception e) {

                }
                String rs = iter + ":" + lock + ":" + st;
                awaiter.then(lock, rs);
            }).start();
            new Thread(() -> {
                try {
                    Object ret = awaiter.await(lock);
                    System.out.println(ret);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

        Integer ret = awaiter.promise((manager, lock) -> {
            new Thread(() -> {

            }).start();
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
            manager.then(lock, 3000);
        });
        System.out.println(ret);

        awaiter.shutdown();
    }
}
