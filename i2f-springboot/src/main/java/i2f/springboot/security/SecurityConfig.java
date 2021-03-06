package i2f.springboot.security;

import i2f.springboot.security.impl.JsonSupportUsernamePasswordAuthenticationFilter;
import i2f.springboot.security.impl.LoginPasswordDecoder;
import i2f.springboot.security.impl.UnAuthorizedHandler;
import i2f.springboot.security.impl.token.AuthenticationTokenFilter;
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
        // ????????????
        if(enableCors){
            http.cors();
            log.info("SecurityConfig enable cors.");
        }else{
            http.cors().disable();
            log.info("SecurityConfig disabled cors.");
        }
        // ??????csrf
        if(enableCsrf){
            http.csrf();
            log.info("SecurityConfig enable csrf.");
        }else{
            http.csrf().disable();
            log.info("SecurityConfig disable csrf.");
        }

        // ??????httpBasic
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

        // ????????????????????????
        if(staticResourceList!=null && !"".equals(staticResourceList)){
            http.authorizeRequests()
                    .antMatchers(HttpMethod.GET,
                            staticResourceList.split(","))
                    .permitAll();
            log.info("SecurityConfig static resource config:"+staticResourceList);
        }

        // ???????????????????????????????????????
        if(anonymousList!=null && !"".equals(anonymousList)){
            http.authorizeRequests()
                    .antMatchers(anonymousList.split(",")).anonymous();
            log.info("SecurityConfig customer anonymous list:"+anonymousList);
        }

        // ???????????????????????????????????????
        if(permitAllList!=null && !"".equals(permitAllList)){
            http.authorizeRequests()
                    .antMatchers(permitAllList.split(",")).permitAll();
            log.info("SecurityConfig customer permit-all list:"+permitAllList);
        }

        // ?????????????????????Session????????????
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
            // ?????????????????????Session??????
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
            // ????????????URL
            http.formLogin()
                    .loginProcessingUrl(loginUrl)
                    .usernameParameter(loginUsername)
                    .passwordParameter(loginUsername)
                    .successHandler(authenticationSuccessHandler);
            log.info("SecurityConfig customer config form-login config.");
        }else{
            // ??????formLogin,????????????????????? UsernamePasswordAuthenticationFilter
            http.formLogin()
                    .disable();
            log.info("SecurityConfig disable form-login.");
        }

        // ????????????JSON?????????????????????,??????????????????
        // ?????????????????????????????????????????????????????????????????????
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

        // ????????????URL??????????????????
        http.authorizeRequests()
                .antMatchers(loginUrl)
                .anonymous();

        if(logoutUrl==null || "".equals(logoutUrl)){
            logoutUrl="/logout";
        }

        // ?????????????????????
        http.logout()
                .logoutUrl(logoutUrl);
        if(logoutSuccessHandler!=null){
            http.logout()
                    .logoutSuccessHandler(logoutSuccessHandler);
            log.info("SecurityConfig customer logout success handler.");
        }

        // ??????token????????????????????????
        if(authenticationTokenFilter!=null){
            http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
            log.info("SecurityConfig customer token filter config.");
        }

        // ???????????????????????????
        if(unauthorizedHandler!=null){
            http.exceptionHandling()
                    .authenticationEntryPoint(unauthorizedHandler);
            log.info("SecurityConfig customer unauthorized handler config.");
        }


        // ???????????????????????????????????????
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
