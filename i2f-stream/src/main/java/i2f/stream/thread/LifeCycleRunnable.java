package i2f.stream.thread;

/**
 * @author Ice2Faith
 * @date 2024/2/26 9:28
 * @desc
 */
public abstract class LifeCycleRunnable<T> implements Runnable {
    protected T resource;
    private Throwable throwable;

    public LifeCycleRunnable(T resource) {
        this.resource = resource;
    }

    @Override
    public void run() {
        onBefore(resource);
        try {
            doTask(resource);
        }catch (Throwable e){
            this.throwable=throwable;
            onThrowable(this.throwable);
        }finally {
            onAfter(resource);
        }
    }

    public abstract void doTask(T resource) throws Throwable;

    public boolean isThrowable(){
        return this.throwable!=null;
    }

    public Throwable getThrowable(){
        return this.throwable;
    }

    public void onBefore(T resource){

    }

    public void onAfter(T resource){

    }

    public void onThrowable(Throwable throwable){
        throw new IllegalStateException("runnable task run exception",throwable);
    }
}
