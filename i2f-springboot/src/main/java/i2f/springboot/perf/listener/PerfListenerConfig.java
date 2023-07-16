package i2f.springboot.perf.listener;

import i2f.springboot.perf.listener.impl.PerfClientIpListener;
import i2f.springboot.perf.listener.impl.PerfRequestListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/6/8 14:01
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.perf.collect.listener.enable:true}")
@Configuration
public class PerfListenerConfig {

    @Bean
    public PerfRequestListener spyRequestListener() {
        return new PerfRequestListener();
    }

    @Bean
    public PerfClientIpListener spyClientIpListener() {
        return new PerfClientIpListener();
    }

    @Bean
    public ServletListenerRegistrationBean<PerfRequestListener> spyRequestListenerServletListenerRegistrationBean() {
        ServletListenerRegistrationBean<PerfRequestListener> ret = new ServletListenerRegistrationBean<>();
        ret.setListener(spyRequestListener());
        return ret;
    }

    @Bean
    public ServletListenerRegistrationBean<PerfClientIpListener> spyClientIpListenerServletListenerRegistrationBean() {
        ServletListenerRegistrationBean<PerfClientIpListener> ret = new ServletListenerRegistrationBean<>();
        ret.setListener(spyClientIpListener());
        return ret;
    }
}
