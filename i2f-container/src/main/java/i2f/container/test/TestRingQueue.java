package i2f.container.test;

import i2f.container.RingQueue;
import i2f.container.reference.Reference;

import java.security.SecureRandom;
import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/4/12 11:30
 * @desc size
 * + ++  + + + + + + + + + +
 * threads=[]
 * tasks=[]
 */
public class TestRingQueue {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            test();
            System.out.println("\n\n\n-------------------------------------------------");
        }
    }

    public static void test() throws Exception {
        RingQueue<Integer> queue = new RingQueue<>(5);

        CountDownLatch allLatch = new CountDownLatch(2);

        new Thread(() -> {
            SecureRandom random = new SecureRandom();
            int num = 0;
            while (true) {
                if (true || !queue.isFull()) {
                    queue.enqueue(num);
                    System.out.println(">>>>>>:" + num);
                    num++;
                    if (num > 500) {
                        queue.enqueue(-1);
                        break;
                    }
                }
                try {
                    Thread.sleep(random.nextInt(30));
                } catch (Exception e) {

                }

            }
            allLatch.countDown();
        }, "producer").start();

        new Thread(() -> {
            SecureRandom random = new SecureRandom();
            int num = 0;
            while (true) {
                if (true || !queue.isEmpty()) {
                    Integer val = queue.dequeue();
                    if (val == 499) {
                        Reference<Integer> head = queue.head();
                        System.out.println("head:" + head);
                        Reference<Integer> tail = queue.tail();
                        System.out.println("tail:" + tail);
                    }
                    System.out.println("<<<<<<:" + val);
                    if (val == -1) {
                        break;
                    }
//                    List<Integer> val = queue.dequeueAllIf(2);
//                    if(val!=null) {
//                        System.out.println("<<<<<<:" + val);
//                        if (val.contains(-1)) {
//                            break;
//                        }
//                    }
                }
                try {
                    Thread.sleep(random.nextInt(60));
                } catch (Exception e) {

                }

            }
            allLatch.countDown();
        }, "consumer").start();

        allLatch.await();
    }
}
