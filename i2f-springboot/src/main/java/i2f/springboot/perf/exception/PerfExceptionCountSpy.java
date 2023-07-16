package i2f.springboot.perf.exception;

import i2f.springboot.perf.data.PerfIndex;
import i2f.springboot.perf.spy.PerfMultiSpy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/6/8 17:57
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.collect.exception.enable:true}")
@Order(-1)
@Component
public class PerfExceptionCountSpy implements HandlerExceptionResolver, PerfMultiSpy {
    private AtomicInteger exceptCount = new AtomicInteger(0);
    private AtomicInteger runtimeCount = new AtomicInteger(0);
    private AtomicInteger ioCount = new AtomicInteger(0);
    private AtomicInteger sqlCount = new AtomicInteger(0);
    private AtomicInteger connCount = new AtomicInteger(0);
    private AtomicInteger securityCount = new AtomicInteger(0);
    private AtomicInteger reflectCount = new AtomicInteger(0);
    private AtomicInteger nullCount = new AtomicInteger(0);
    private AtomicInteger castCount = new AtomicInteger(0);

    @Override
    public List<PerfIndex> collect() {
        List<PerfIndex> ret = new ArrayList<>();
        ret.add(new PerfIndex("Exception:count:all", exceptCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:runtime", runtimeCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:io", ioCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:sql", sqlCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:connect", connCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:security", securityCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:reflect", reflectCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:null", nullCount.getAndSet(0)));
        ret.add(new PerfIndex("Exception:count:cast", castCount.getAndSet(0)));

        return ret;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception e) {
        exceptCount.incrementAndGet();
        if (e instanceof RuntimeException) {
            runtimeCount.incrementAndGet();
        }
        if (e instanceof IOException) {
            ioCount.incrementAndGet();
        }
        if (e instanceof SQLException) {
            sqlCount.incrementAndGet();
        }
        if (e instanceof ConnectException) {
            connCount.incrementAndGet();
        }
        if (e instanceof SecurityException) {
            securityCount.incrementAndGet();
        }
        if (e instanceof ReflectiveOperationException) {
            reflectCount.incrementAndGet();
        }
        if (e instanceof NullPointerException) {
            nullCount.incrementAndGet();
        }
        if (e instanceof ClassCastException) {
            castCount.incrementAndGet();
        }
        return null;
    }
}
