package i2f.springboot.perf.listener.impl;

import i2f.springboot.perf.spy.PerfSpy;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/6/8 13:54
 * @desc
 */
public class PerfRequestListener implements ServletRequestListener, PerfSpy {

    private AtomicInteger requestCount = new AtomicInteger(0);

    @Override
    public String name() {
        return "Request:Count";
    }

    @Override
    public double collect() {
        return requestCount.get();
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        requestCount.incrementAndGet();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        requestCount.decrementAndGet();
    }

}
