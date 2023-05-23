package i2f.springboot.trace.spy.plugin;

import i2f.springboot.trace.spy.SpringTraceLogger;
import i2f.springboot.trace.spy.core.InvokeTrace;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2023/5/22 16:15
 * @desc
 */
@ConditionalOnExpression("${invoke.trace.filter.enable:false}")
@Slf4j
@Data
@Component
@WebFilter(urlPatterns = "/**")
public class FilterTrace extends OncePerRequestFilter {
    public static Method filterMethod = null;
    private AtomicBoolean excluded = new AtomicBoolean(false);

    static {
        try {
            Class<? extends FilterTrace> clazz = FilterTrace.class;
            filterMethod = clazz.getDeclaredMethod("doFilterInternal", HttpServletRequest.class, HttpServletResponse.class, FilterChain.class);
        } catch (Exception e) {
            log.warn("init default trace method failure:" + e.getMessage(), e);
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (!excluded.get()) {
            for (Object bean : CglibProxyTrace.enhancerBeans.values()) {
                if (bean.equals(this)) {
                    excluded.set(true);
                }
            }
        }
        if (excluded.get()) {
            chain.doFilter(request, response);
            return;
        }
        Object[] args = {request, response, chain};
        try {
            String line = request.getMethod() + " " + request.getRequestURI();
            String qs = request.getQueryString();
            if (qs != null && !"".equals(qs)) {
                line += "?" + qs;
            }
            String remark = line;
            InvokeTrace.trace(() -> {
                chain.doFilter(request, response);
                return null;
            }, (meta) -> {
                meta.remark = remark;
                SpringTraceLogger.LOGGER.accept(meta);
            }, this.getClass(), filterMethod, args);
        } catch (Throwable e) {
            throw new ServletException("filter trace error:" + e.getMessage(), e);
        }
    }
}
