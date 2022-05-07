package i2f.core.thread.impl;

/**
 * @author ltb
 * @date 2022/5/7 11:23
 * @desc
 */
public abstract class ArgsRunnable implements Runnable{
    protected Object[] args;
    public ArgsRunnable(Object ... args){
        this.args=args;
    }
    @Override
    final public void run() {
        doTask(args);
    }

    public abstract void doTask(Object ... args);
}
