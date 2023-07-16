package i2f.springboot.perf.filter;

import i2f.core.thread.NamingThreadFactory;
import i2f.springboot.perf.PerfConfig;
import i2f.springboot.perf.context.PerfStorage;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Ice2Faith
 * @date 2023/6/8 8:52
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.collect.qps.enable:true}")
@Slf4j
@Data
@Component
@WebFilter(urlPatterns = "/**")
public class PerfQpsFilter extends OncePerRequestFilter {
    public static final String X_REAL_FORWARD_URL="X-REAL-FORWARD-URL";

    private ConcurrentHashMap<String, AtomicInteger> statisticMap = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(3,new NamingThreadFactory("perf","qps"));

    {
        pool.scheduleAtFixedRate(() -> {
            for (String name : statisticMap.keySet()) {
                AtomicInteger value = statisticMap.get(name);
                PerfConfig.STORE.add(name, value.getAndSet(0));
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        Object realUrlAttr = request.getAttribute(X_REAL_FORWARD_URL);
        if(realUrlAttr!=null){
            String realUrl=String.valueOf(realUrlAttr);
            if(!StringUtils.isEmpty(realUrl)){
                uri=realUrl;
            }
        }
        String name = PerfStorage.QPS_PREFIX + uri;
        if (!statisticMap.containsKey(name)) {
            statisticMap.put(name, new AtomicInteger(0));
        }
        statisticMap.get(name).incrementAndGet();

        if (!statisticMap.containsKey(PerfStorage.GLOBAL_QPS)) {
            statisticMap.put(PerfStorage.GLOBAL_QPS, new AtomicInteger(0));
        }
        statisticMap.get(PerfStorage.GLOBAL_QPS).incrementAndGet();

        chain.doFilter(request, response);
    }
}
