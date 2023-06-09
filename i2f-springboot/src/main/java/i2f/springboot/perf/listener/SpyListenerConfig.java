package i2f.springboot.perf.listener;

import i2f.springboot.perf.listener.impl.SpyClientIpListener;
import i2f.springboot.perf.listener.impl.SpyRequestListener;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Ice2Faith
 * @date 2023/6/8 14:01
 * @desc
 */
@Configuration
public class SpyListenerConfig {

    @Bean
    public SpyRequestListener spyRequestListener() {
        return new SpyRequestListener();
    }

    @Bean
    public SpyClientIpListener spyClientIpListener() {
        return new SpyClientIpListener();
    }

    @Bean
    public ServletListenerRegistrationBean<SpyRequestListener> spyRequestListenerServletListenerRegistrationBean() {
        ServletListenerRegistrationBean<SpyRequestListener> ret = new ServletListenerRegistrationBean<>();
        ret.setListener(spyRequestListener());
        return ret;
    }

    @Bean
    public ServletListenerRegistrationBean<SpyClientIpListener> spyClientIpListenerServletListenerRegistrationBean() {
        ServletListenerRegistrationBean<SpyClientIpListener> ret = new ServletListenerRegistrationBean<>();
        ret.setListener(spyClientIpListener());
        return ret;
    }
}
