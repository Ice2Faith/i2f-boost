package i2f.springboot.shiro;

import i2f.core.reflect.core.ReflectResolver;
import i2f.spring.environment.EnvironmentUtil;
import i2f.springboot.security.impl.LoginPasswordDecoder;
import i2f.springboot.shiro.def.DefaultLoginSuccessHandler;
import i2f.springboot.shiro.def.DefaultLogoutHandler;
import i2f.springboot.shiro.filter.ShiroCoreFilter;
import i2f.springboot.shiro.handler.ILoginFailureHandler;
import i2f.springboot.shiro.handler.ILoginSuccessHandler;
import i2f.springboot.shiro.handler.ILogoutHandler;
import i2f.springboot.shiro.impl.UsernamePasswordRealm;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import i2f.springboot.shiro.token.CustomerTokenRealm;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.mgt.*;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
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
    public static final String SHIRO_CORE_FILTER_NAME ="shiroCoreFiler";
    public static final String SHIRO_FILTER_CONF_PREFIX="i2f.springboot.config.shiro.filters.";

    @Autowired
    private Environment environment;

    @Autowired
    private UsernamePasswordRealm usernamePasswordRealm;

    @Autowired(required = false)
    private CustomerTokenRealm customerTokenRealm;

    @Autowired(required = false)
    private AbstractShiroTokenHolder tokenHolder;

    private boolean enableSingleLogin=true;

    private String tokenName="token";

    private String matcherAlgoName="MD5";
    private int matcherIterations=256;

    private String loginUrl="/login";
    private String unauthorizedUrl="/unauth";
    private String logoutUrl="/logout";

    private String usernameParameter ="username";
    private String passwordParameter ="password";

    private String staticResourceWhiteList;
    private String customerWhiteList;

    private boolean enableSession=false;

    @Autowired(required = false)
    private LoginPasswordDecoder passwordDecoder;

    @Autowired
    private ILoginSuccessHandler loginSuccessHandler;

    @Autowired
    private ILoginFailureHandler loginFailureHandler;

    @Autowired
    private ILogoutHandler logoutHandler;

    // ???????????????????????????
    @Bean
    public SecurityManager securityManager(){
        List<Realm> realms=new ArrayList<>();
        // ????????????????????????????????????????????????????????????????????????

        if(customerTokenRealm!=null){
            log.info("ShiroConfig customerTokenRealm find.");
            realms.add(customerTokenRealm);
        }

        // ?????????????????????????????????????????????
        usernamePasswordRealm.setCredentialsMatcher(matcher());
        realms.add(usernamePasswordRealm);

        // ??????web???????????????
        DefaultWebSecurityManager manager=new DefaultWebSecurityManager();
        manager.setRealms(realms);

        // ???????????????session?????????
        manager.setSubjectFactory(subjectFactory());
        SubjectDAO subjectDAO=manager.getSubjectDAO();
        if(subjectDAO instanceof DefaultSubjectDAO){
            DefaultSubjectDAO dao=(DefaultSubjectDAO)subjectDAO;
            SessionStorageEvaluator evaluator=dao.getSessionStorageEvaluator();
            if(evaluator instanceof DefaultSessionStorageEvaluator){
                DefaultSessionStorageEvaluator def=(DefaultSessionStorageEvaluator)evaluator;
                def.setSessionStorageEnabled(enableSession);
            }
        }

        log.info("ShiroConfig SecurityManager config done.");
        return manager;
    }

    // ??????session-less?????????subjectFactory
    @Bean
    public SubjectFactory subjectFactory(){
        SessionControlWebSubjectFactory factory=new SessionControlWebSubjectFactory(enableSession);
        log.info("ShiroConfig SessionControlWebSubjectFactory config done.");
        return factory;
    }

    // ?????????????????????????????????????????????????????????@RequiresRoles
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(){
        AuthorizationAttributeSourceAdvisor advisor=new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager());
        log.info("ShiroConfig AuthorizationAttributeSourceAdvisor config done.");
        return advisor;
    }

    // ???????????????????????????????????????????????????
    @Bean
    public CredentialsMatcher matcher(){
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName(matcherAlgoName);
        matcher.setHashIterations(matcherIterations);
        log.info("ShiroConfig CredentialsMatcher config done.");
        return matcher;
    }

    // ???????????????????????????????????????
    @Bean
    public HashedCredentialsMatcherEncoder matcherEncoder(){
        HashedCredentialsMatcherEncoder encoder=new HashedCredentialsMatcherEncoder((HashedCredentialsMatcher)matcher());
        log.info("ShiroConfig HashedCredentialsMatcherEncoder config done.");
        return encoder;
    }

    // ?????????????????????????????????
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean bean=new ShiroFilterFactoryBean();

        bean.setSecurityManager(securityManager());
        bean.setLoginUrl(loginUrl);
        bean.setUnauthorizedUrl(unauthorizedUrl);

        StringBuilder chains=new StringBuilder();
        // ???????????????
        chains.append("\n");
        chains.append(logoutUrl).append("=logout");
        // ???????????????
        chains.append("\n");
        chains.append(loginUrl).append("=anon");
        // ??????????????????
        chains.append("\n");
        chains.append(unauthorizedUrl).append("=anon");

        // ?????????????????????
        if(staticResourceWhiteList!=null && !"".equals(staticResourceWhiteList)){
            String[] items=staticResourceWhiteList.split(",");
            for(String item : items){
                if(!"".equals(item)){
                    chains.append("\n");
                    chains.append(item).append("=anon");
                }
            }
        }

        // ??????????????????
        if(customerWhiteList!=null && !"".equals(customerWhiteList)){
            String[] items=customerWhiteList.split(",");
            for(String item : items){
                if(!"".equals(item)){
                    chains.append("\n");
                    chains.append(item).append("=anon");
                }
            }
        }

        // ????????????????????????
        chains.append("\n");
        chains.append("/**=authc");

        bean.setFilterChainDefinitions(chains.toString());

        Map<String, Filter> filters=new HashMap<>();
        log.info("ShiroConfig filters "+ SHIRO_CORE_FILTER_NAME +" config done.");

        if(logoutHandler instanceof DefaultLogoutHandler){
            DefaultLogoutHandler handler=(DefaultLogoutHandler)logoutHandler;
            handler.setTokenName(tokenName);
        }

        if(loginSuccessHandler instanceof DefaultLoginSuccessHandler){
            DefaultLoginSuccessHandler handler=(DefaultLoginSuccessHandler)loginSuccessHandler;
            handler.setEnableSingleLogin(enableSingleLogin);
        }

        ShiroCoreFilter tokenFilter=new ShiroCoreFilter()
                .setTokenName(tokenName)
                .setLoginUrl(loginUrl)
                .setUsernameParameter(usernameParameter)
                .setPasswordParameter(passwordParameter)
                .setPasswordDecoder(passwordDecoder)
                .setLoginSuccessHandler(loginSuccessHandler)
                .setLoginFailureHandler(loginFailureHandler)
                .setLogoutUrl(logoutUrl)
                .setLogoutHandler(logoutHandler)
                .setEnableProcessToken(customerTokenRealm!=null)
                .setTokenHolder(tokenHolder);

        filters.put(SHIRO_CORE_FILTER_NAME,tokenFilter);
        Map<String,Filter> filterMap=getFilters();
        filters.putAll(filterMap);

        bean.setFilters(filters);

        List<String> globalFilters=new ArrayList<>();
        globalFilters.add(SHIRO_CORE_FILTER_NAME);
        for(String item : filterMap.keySet()){
            globalFilters.add(item);
        }

        bean.setGlobalFilters(globalFilters);
        log.info("ShiroConfig ShiroFilterFactoryBean config done.");
        return bean;
    }

    // ???????????????????????????
    @Bean
    public DelegatingFilterProxy filter(){
        DelegatingFilterProxy filter=new DelegatingFilterProxy();
        filter.setTargetBeanName("shiroFilter");
        log.info("ShiroConfig DelegatingFilterProxy config done.");
        return filter;
    }

    // ??????????????????????????????????????????????????????
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
        Map<String,Object> conf=EnvironmentUtil.of(environment).getPropertiesWithPrefix(false,SHIRO_FILTER_CONF_PREFIX);
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
