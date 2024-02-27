package i2f.stream.thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author Ice2Faith
 * @date 2024/2/26 9:31
 * @desc
 */
public abstract class CountDownLatchCallable<R> extends LifeCycleCallable<CountDownLatch,R> {
    public CountDownLatchCallable(CountDownLatch resource) {
        super(resource);
    }

    @Override
    public void onBefore(CountDownLatch resource) {

    }

    @Override
    public void onAfter(CountDownLatch resource) {
        if(resource!=null){
            resource.countDown();
        }
    }
}
