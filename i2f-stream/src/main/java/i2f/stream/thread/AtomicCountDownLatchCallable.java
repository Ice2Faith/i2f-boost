package i2f.stream.thread;

/**
 * @author Ice2Faith
 * @date 2024/2/26 9:31
 * @desc
 */
public abstract class AtomicCountDownLatchCallable<R> extends LifeCycleCallable<AtomicCountDownLatch,R> {
    public AtomicCountDownLatchCallable(AtomicCountDownLatch resource) {
        super(resource);
    }

    @Override
    public void onBefore(AtomicCountDownLatch resource) {

    }

    @Override
    public void onAfter(AtomicCountDownLatch resource) {
        if(resource!=null){
            System.out.println("down:"+Thread.currentThread().getName()+"-"+Thread.currentThread().getId());
            resource.down();
        }
    }
}
