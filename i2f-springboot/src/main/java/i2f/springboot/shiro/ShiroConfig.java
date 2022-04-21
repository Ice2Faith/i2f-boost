package i2f.springboot.shiro;

import i2f.core.reflect.core.ReflectResolver;
import i2f.spring.environment.EnvironmentUtil;
import i2f.springboot.shiro.filter.ShiroTokenFilter;
import i2f.springboot.shiro.impl.UsernamePasswordRealm;
import i2f.springboot.shiro.token.CustomerTokenRealm;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/2/25 8:48
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.shiro.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "i2f.springboot.config.shiro")
public class ShiroConfig  {
    public static final String SHIRO_TOKEN_FILTER_NAME="tokenLoginFiler";
    public static final String SHIRO_FILTER_CONF_PREFIX="i2f.springboot.config.shiro.filters.";

    @Autowired
    private Environment environment;

    @Autowired
    private UsernamePasswordRealm usernamePasswordRealm;

    @Autowired(required = false)
    private CustomerTokenRealm customerTokenRealm;

    private String tokenName="token";

    private String matcherAlgoName="MD5";
    private int matcherIterations=256;

    private String loginUrl="/login";
    private String unauthorizedUrl="/unauth";
    private String logoutUrl="/logout";

    private String staticResourceWhiteList;
    private String customerWhiteList;

    // 得到核心安全控制器
    @Bean
    public SecurityManager securityManager(){
        List<Realm> realms=new ArrayList<>();
        // 在此处添加其他验证器，这样其他验证器的优先级较高

        if(customerTokenRealm!=null){
            log.info("ShiroConfig customerTokenRealm find.");
            realms.add(customerTokenRealm);
        }

        // 添加一个默认的用户名密码验证器
        usernamePasswordRealm.setCredentialsMatcher(matcher());
        realms.add(usernamePasswordRealm);

        // 配置web安全控制器
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealms(realms);

        log.info("ShiroConfig SecurityManager config done.");
        return manager;
    }


    // 得到用户名密码登录方式的密码匹配器
    @Bean
    public CredentialsMatcher matcher(){
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(matcherAlgoName);
        matcher.setHashIterations(matcherIterations);
        log.info("ShiroConfig CredentialsMatcher config done.");
        return matcher;
    }

    // 得到密码匹配器的密码生成器
    @Bean
    public HashedCredentialsMatcherEncoder matcherEncoder(){
        HashedCredentialsMatcherEncoder encoder=new HashedCredentialsMatcherEncoder((HashedCredentialsMatcher)matcher());
        log.info("ShiroConfig HashedCredentialsMatcherEncoder config done.");
        return encoder;
    }

    // 得到核心过滤器过滤规则
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();

        bean.setSecurityManager(securityManager());
        bean.setLoginUrl(loginUrl);
        bean.setUnauthorizedUrl(unauthorizedUrl);

        StringBuilder chains=new StringBuilder();
        // 登出白名单
        chains.append("\n");
        chains.append(logoutUrl).append("=logout");
        // 登录白名单
        chains.append("\n");
        chains.append(loginUrl).append("=anon");
        // 无权限白名单
        chains.append("\n");
        chains.append(unauthorizedUrl).append("=anon");

        // 静态资源白名单
        if(staticResourceWhiteList!=null && !"".equals(staticResourceWhiteList)){
            String[] items=staticResourceWhiteList.split(",");
            for(String item : items){
                if(!"".equals(item)){
                    chains.append("\n");
                    chains.append(item).append("=anon");
                }
            }
        }

        // 自定义白名单
        if(customerWhiteList!=null && !"".equals(customerWhiteList)){
            String[] items=customerWhiteList.split(",");
            for(String item : items){
                if(!"".equals(item)){
                    chains.append("\n");
                    chains.append(item).append("=anon");
                }
            }
        }

        // 剩余的都需要权限
        chains.append("\n");
        chains.append("/**=authc");

        bean.setFilterChainDefinitions(chains.toString());

        Map<String, Filter> filters=new HashMap<>();
        if(customerTokenRealm!=null){
            log.info("ShiroConfig filters "+SHIRO_TOKEN_FILTER_NAME+" config done.");
            filters.put(SHIRO_TOKEN_FILTER_NAME,new ShiroTokenFilter(tokenName));
        }
        filters.putAll(getFilters());

        bean.setFilters(filters);
        log.info("ShiroConfig ShiroFilterFactoryBean config done.");
        return bean;
    }

    // 得到核心过滤器对象
    @Bean
    public DelegatingFilterProxy filter(){
        DelegatingFilterProxy filter=new DelegatingFilterProxy();
        filter.setTargetBeanName("shiroFilter");
        log.info("ShiroConfig DelegatingFilterProxy config done.");
        return filter;
    }

    // 注册核心过滤器到环境中，拦截所有请求
    @Bean
    public FilterRegistrationBean shiroAuthFilter(){
        FilterRegistrationBean registry=new FilterRegistrationBean();
        registry.setFilter(filter());
        registry.addUrlPatterns("/*");
        registry.setOrder(1);
        log.info("ShiroConfig FilterRegistrationBean config done.");
        return registry;
    }

    private Map<String,Filter> getFilters(){
        Map<String,Object> conf=EnvironmentUtil.getPropertiesWithPrefix(environment,false,SHIRO_FILTER_CONF_PREFIX);
        Map<String,Filter> ret=new HashMap<>();

        for(Map.Entry<String, Object> item : conf.entrySet()){
            String name=item.getKey();
            String className=(String)item.getValue();
            try{
                Class clazz= ReflectResolver.getClazz(className);
                Filter ins= (Filter)ReflectResolver.instance(clazz);
                ret.put(name,ins);
                log.info("ShiroConfig find filter called :"+name+" use class :"+className);
            }catch(Exception e){
                log.warn("shiroConfig find filter with bad added which called:"+name+" use class:"+className);
            }
        }

        return ret;
    }
}
