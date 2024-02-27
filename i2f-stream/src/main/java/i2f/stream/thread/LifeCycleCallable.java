package i2f.stream.thread;

import java.util.concurrent.Callable;

/**
 * @author Ice2Faith
 * @date 2024/2/26 10:03
 * @desc
 */
public abstract class LifeCycleCallable<T,R> implements Callable<R> {

    protected T resource;
    private Throwable throwable;

    public LifeCycleCallable(T resource) {
        this.resource = resource;
    }

    @Override
    public R call() {
        onBefore(resource);
        try {
            return doTask(resource);
        }catch (Throwable e){
            this.throwable=throwable;
            onThrowable(this.throwable);
            throw new IllegalStateException("callable task run exception",throwable);
        }finally {
            onAfter(resource);
        }
    }

    public abstract R doTask(T resource) throws Throwable;

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
