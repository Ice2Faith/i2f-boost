package i2f.extension.spring.springboot.config.security;

import i2f.core.api.ApiResp;
import i2f.core.net.http.HttpStatus;
import i2f.extension.spring.springboot.config.security.impl.JsonSupportUsernamePasswordAuthenticationFilter;
import i2f.extension.spring.springboot.config.security.impl.LoginPasswordDecoder;
import i2f.extension.spring.springboot.config.security.impl.UnAuthorizedHandler;
import i2f.extension.spring.springboot.config.security.impl.token.AuthenticationTokenFilter;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ltb
 * @date 2022/2/25 8:48
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.security.enable:true}")
@Slf4j
@Data
@NoArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true,jsr250Enabled = true)
@Configuration
@ControllerAdvice
@ConfigurationProperties(prefix = "i2f.springboot.config.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${i2f.springboot.config.security.access-deny-exception-handler.enable:true}")
    private boolean enableAccessDenyExceptionHandle;

    // 处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Object exceptCatch(Exception e) throws Exception {
        if(!enableAccessDenyExceptionHandle){
            throw e;
        }
        e.printStackTrace();
        return ApiResp.error(HttpStatus.FORBIDDEN,"access deny for access resource,permission missing!");
    }

    @Value("${i2f.springboot.config.security.csrf.enable:false}")
    private boolean enableCsrf=false;

    @Value("${i2f.springboot.config.security.cors.enable:true}")
    private boolean enableCors=true;

    @Value("${i2f.springboot.config.security.form-login.enable:true}")
    private boolean enableFormLogin=true;

    @Value("${i2f.springboot.config.security.http-basic.enable:false}")
    private boolean enableHttpBasic=false;

    @Value("${i2f.springboot.config.security.login-json.enable:true}")
    private boolean enableJsonLogin=true;

    private String ignoreList;
    private String anonymousList;
    private String permitAllList;
    private String staticResourceList;

    private String sessionCreationPolicy;

    private String loginUrl;
    private String loginUsername;
    private String loginPassword;

    private String logoutUrl;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required = false)
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired(required = false)
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Autowired
    private UnAuthorizedHandler unauthorizedHandler;

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired(required = false)
    private LoginPasswordDecoder loginPasswordDecoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
        log.info("SecurityConfig userDetailsService,passwordEncoder done.");
    }

    @Override
    public void init(WebSecurity web) throws Exception {
        super.init(web);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        if(ignoreList!=null && !"".equals(ignoreList)){
            web.ignoring()
                    .antMatchers(ignoreList.split(","));
            log.info("SecurityConfig ignore list:"+ignoreList);
        }else{
            super.configure(web);
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 配置跨域
        if(enableCors){
            http.cors();
            log.info("SecurityConfig enable cors.");
        }else{
            http.cors().disable();
            log.info("SecurityConfig disabled cors.");
        }
        // 配置csrf
        if(enableCsrf){
            http.csrf();
            log.info("SecurityConfig enable csrf.");
        }else{
            http.csrf().disable();
            log.info("SecurityConfig disable csrf.");
        }

        // 配置httpBasic
        if(enableHttpBasic){
            http.httpBasic();
            log.info("SecurityConfig enable http-basic.");
        }else{
            http.httpBasic().disable();
            log.info("SecurityConfig disable http-basic.");
        }

        if(staticResourceList==null){
            staticResourceList="/**/*.html,/**/*.css,/**/*.js,/**/*.png,/**/*.jpg,/**/*.jpeg,/**/*.ttf,/**/*.woff,/**/*.woff2";
            log.info("SecurityConfig default static resource config.");
        }

        // 配置静态资源访问
        if(staticResourceList!=null && !"".equals(staticResourceList)){
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET,
                            staticResourceList.split(","))
                    .permitAll();
            log.info("SecurityConfig static resource config:"+staticResourceList);
        }

        // 配置用户定义匿名访问白名单
        if(anonymousList!=null && !"".equals(anonymousList)){
            http.authorizeRequests()
                    .antMatchers(anonymousList.split(",")).anonymous();
            log.info("SecurityConfig customer anonymous list:"+anonymousList);
        }

        // 配置用户定义完全访问白名单
        if(permitAllList!=null && !"".equals(permitAllList)){
            http.authorizeRequests()
                    .antMatchers(permitAllList.split(",")).permitAll();
            log.info("SecurityConfig customer permit-all list:"+permitAllList);
        }

        // 配置用户自定义Session管理方式
        if(sessionCreationPolicy!=null && !"".equals(sessionCreationPolicy)){
            SessionCreationPolicy policy=SessionCreationPolicy.STATELESS;
            sessionCreationPolicy=sessionCreationPolicy.toUpperCase().trim();
            if(String.valueOf(SessionCreationPolicy.STATELESS).equals(sessionCreationPolicy)){
                policy=SessionCreationPolicy.STATELESS;
            }else if(String.valueOf(SessionCreationPolicy.ALWAYS).equals(sessionCreationPolicy)){
                policy=SessionCreationPolicy.ALWAYS;
            }else if(String.valueOf(SessionCreationPolicy.NEVER).equals(sessionCreationPolicy)){
                policy=SessionCreationPolicy.NEVER;
            }else if(String.valueOf(SessionCreationPolicy.IF_REQUIRED).equals(sessionCreationPolicy)){
                policy=SessionCreationPolicy.IF_REQUIRED;
            }
            http.sessionManagement()
                    .sessionCreationPolicy(policy);
            log.info("SecurityConfig customer session policy:"+policy);
        }else{
            // 配置默认无状态Session方式
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            log.info("SecurityConfig default session policy:"+SessionCreationPolicy.STATELESS);
        }

        if(loginUrl==null || "".equals(loginUrl)){
            loginUrl="/login";
        }

        if(loginUsername==null || "".equals(loginUsername)){
            loginUsername="username";
        }

        if(loginPassword==null || "".equals(loginPassword)){
            loginPassword="password";
        }

        if(enableFormLogin){
            // 配置登录URL
            http.formLogin()
                    .loginProcessingUrl(loginUrl)
                    .usernameParameter(loginUsername)
                    .passwordParameter(loginUsername)
                    .successHandler(authenticationSuccessHandler);
            log.info("SecurityConfig customer config form-login config.");
        }else{
            // 禁用formLogin,也就不会再进入 UsernamePasswordAuthenticationFilter
            http.formLogin()
                    .disable();
            log.info("SecurityConfig disable form-login.");
        }

        // 配置支持JSON方式提交的表单,替换默认方式
        // 使用自定义方式时，需要注意参数需要自己补充进去
        if(enableJsonLogin){
            http.addFilterAt(new JsonSupportUsernamePasswordAuthenticationFilter()
                    .buildAuthenticationManager(authenticationManagerBean())
                    .buildAuthenticationSuccessHandler(authenticationSuccessHandler)
                    .buildLoginPath(loginUrl)
                    .buildParameterUsername(loginUsername)
                    .buildParameterPassword(loginPassword)
                    .buildLoginPasswordDecoder(loginPasswordDecoder),
                    UsernamePasswordAuthenticationFilter.class);
            log.info("SecurityConfig customer json support username-password auth filter.");
        }

        // 配置登录URL允许匿名访问
        http.authorizeRequests()
                .antMatchers(loginUrl)
                .anonymous();

        if(logoutUrl==null || "".equals(logoutUrl)){
            logoutUrl="/logout";
        }

        // 配置登出处理器
        http.logout()
                .logoutUrl(logoutUrl);
        if(logoutSuccessHandler!=null){
            http.logout()
                    .logoutSuccessHandler(logoutSuccessHandler);
            log.info("SecurityConfig customer logout success handler.");
        }

        // 配置token预解析认证过滤器
        if(authenticationTokenFilter!=null){
            http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
            log.info("SecurityConfig customer token filter config.");
        }

        // 配置认证失败处理类
        if(unauthorizedHandler!=null){
            http.exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler);
            log.info("SecurityConfig customer unauthorized handler config.");
        }


        // 配置除了白名单的都需要鉴权
        http.authorizeRequests()
                .anyRequest().authenticated();

    }

    @ConditionalOnMissingBean(AuthenticationManager.class)
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
