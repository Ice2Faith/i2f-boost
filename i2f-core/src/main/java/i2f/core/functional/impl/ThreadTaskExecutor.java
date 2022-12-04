package i2f.core.functional.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.functional.common.IExecutor;

import java.util.concurrent.ExecutorService;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Author("i2f")
public class ThreadTaskExecutor implements IExecutor<Runnable> {
    private ExecutorService pool;

    public ThreadTaskExecutor() {

    }

    public ThreadTaskExecutor(ExecutorService service) {
        this.pool = service;
    }

    @Override
    public void accept(Runnable val) {
        if (this.pool != null) {
            pool.submit(val);
        } else {
            new Thread(val).start();
        }
    }
}
