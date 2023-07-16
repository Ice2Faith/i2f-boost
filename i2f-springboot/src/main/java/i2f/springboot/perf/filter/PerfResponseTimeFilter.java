package i2f.springboot.perf.filter;

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

/**
 * @author Ice2Faith
 * @date 2023/6/8 8:52
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.collect.response-time.enable:true}")
@Slf4j
@Data
@Component
@WebFilter(urlPatterns = "/**")
public class PerfResponseTimeFilter extends OncePerRequestFilter {
    public static final String X_REAL_FORWARD_URL="X-REAL-FORWARD-URL";

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
        String name = PerfStorage.RESPONSE_TIME_PREFIX + uri;

        long beginTime = System.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long endTime = System.currentTimeMillis();
            long useTime = endTime - beginTime;
            PerfConfig.STORE.add(name, useTime);
        }
    }
}
