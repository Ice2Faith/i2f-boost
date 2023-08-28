package i2f.springboot.firewall;

import i2f.core.j2ee.firewall.FirewallFilter;
import i2f.core.j2ee.firewall.context.FirewallContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.DispatcherType;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/8/28 22:00
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.firewall.enable:true}")
@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "i2f.springboot.config.firewall")
public class FirewallConfig implements InitializingBean {

    @Autowired
    private AutowireCapableBeanFactory autowireCapableBeanFactory;

    private boolean enableUrl = true;
    private boolean enableMethod = true;
    private boolean enableMultipart = true;
    private boolean enableParameter = true;
    private boolean enableQueryString = true;

    private Set<String> addBadSuffixes = new HashSet<>();
    private Set<String> removeBadSuffixes = new HashSet<>();

    private Set<String> addBadFilenames = new HashSet<>();
    private Set<String> removeBadFilenames = new HashSet<>();


    @Override
    public void afterPropertiesSet() throws Exception {
        FirewallContext.enableUrl = enableUrl;
        FirewallContext.enableMethod = enableMethod;
        FirewallContext.enableMultipart = enableMultipart;
        FirewallContext.enableParameter = enableParameter;
        FirewallContext.enableQueryString = enableQueryString;
        if (addBadSuffixes != null) {
            FirewallContext.addBadSuffixes.addAll(addBadSuffixes);
        }
        if (removeBadSuffixes != null) {
            FirewallContext.removeBadSuffixes.addAll(removeBadSuffixes);
        }

        if (addBadFilenames != null) {
            FirewallContext.addBadFilenames.addAll(addBadFilenames);
        }
        if (removeBadFilenames != null) {
            FirewallContext.removeBadFilenames.addAll(removeBadFilenames);
        }
        log.info("FirewallConfig config done.");
    }

    @Bean
    public FilterRegistrationBean<FirewallFilter> filterFilterRegistrationBean() {
        FirewallFilter filter = new FirewallFilter();
        autowireCapableBeanFactory.autowireBean(filter);
        FilterRegistrationBean<FirewallFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/**");
        registrationBean.setOrder(-1);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD);
        log.info("FirewallFilter config done.");
        return registrationBean;
    }
}
