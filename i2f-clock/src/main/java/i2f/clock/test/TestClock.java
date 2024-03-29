package i2f.clock.test;


import i2f.clock.SystemClock;
import i2f.clock.TimeCounter;

/**
 * @author Ice2Faith
 * @date 2024/3/25 10:11
 * @desc
 */
public class TestClock {

    public static void testTs(TimeCounter tcs, TimeCounter tcc){
        int maxCount=10000000;
        System.out.println("--------------------");

        tcc.begin();
        for (int i = 0; i < maxCount; i++) {
            SystemClock.currentTimeMillis();
        }
        tcc.end();
        System.out.println("clock :"+tcc);


        tcs.begin();
        for (int i = 0; i < maxCount; i++) {
            System.currentTimeMillis();
        }
        tcs.end();
        System.out.println("system:"+tcs);
        System.out.println("rate  :"+tcc.toCompareRateString(tcs));
        System.out.println("acc   :"+tcs.toCompareRateString(tcc));
    }

    public static void main(String[] args) throws InterruptedException {

        TimeCounter tcs=new TimeCounter();
        TimeCounter tcc=new TimeCounter();
        for (int i = 0; i < 10000; i++) {
            testTs(tcs,tcc);
        }


    }
}
