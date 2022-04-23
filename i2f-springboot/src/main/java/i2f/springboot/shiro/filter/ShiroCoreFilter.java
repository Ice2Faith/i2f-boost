package i2f.springboot.shiro.filter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.core.j2ee.web.ServletContextUtil;
import i2f.springboot.security.impl.LoginPasswordDecoder;
import i2f.springboot.shiro.handler.ILoginFailureHandler;
import i2f.springboot.shiro.handler.ILoginSuccessHandler;
import i2f.springboot.shiro.handler.ILogoutHandler;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import i2f.springboot.shiro.token.CustomerAuthToken;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/21 18:38
 * @desc
 */
@Data
@NoArgsConstructor
public class ShiroCoreFilter extends OncePerRequestFilter {
    private String tokenName="token";
    private String loginUrl="/login";
    private String logoutUrl="/logout";

    private String usernameParameter ="username";
    private String passwordParameter ="password";

    private LoginPasswordDecoder passwordDecoder;

    private ILoginSuccessHandler loginSuccessHandler;

    private ILoginFailureHandler loginFailureHandler;

    private ILogoutHandler logoutHandler;

    private boolean enableProcessToken=true;

    private AbstractShiroTokenHolder tokenHolder;

    public ShiroCoreFilter setTokenName(String tokenName) {
        this.tokenName = tokenName;
        return this;
    }

    public ShiroCoreFilter setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
        return this;
    }

    public ShiroCoreFilter setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
        return this;
    }

    public ShiroCoreFilter setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
        return this;
    }

    public ShiroCoreFilter setPasswordParameter(String passwordParameter) {
        this.passwordParameter = passwordParameter;
        return this;
    }

    public ShiroCoreFilter setPasswordDecoder(LoginPasswordDecoder passwordDecoder) {
        this.passwordDecoder = passwordDecoder;
        return this;
    }

    public ShiroCoreFilter setLoginSuccessHandler(ILoginSuccessHandler loginSuccessHandler) {
        this.loginSuccessHandler = loginSuccessHandler;
        return this;
    }

    public ShiroCoreFilter setLoginFailureHandler(ILoginFailureHandler loginFailureHandler) {
        this.loginFailureHandler = loginFailureHandler;
        return this;
    }

    public ShiroCoreFilter setLogoutHandler(ILogoutHandler logoutHandler) {
        this.logoutHandler = logoutHandler;
        return this;
    }

    public ShiroCoreFilter setEnableProcessToken(boolean enableProcessToken) {
        this.enableProcessToken = enableProcessToken;
        return this;
    }

    public ShiroCoreFilter setTokenHolder(AbstractShiroTokenHolder tokenHolder) {
        this.tokenHolder = tokenHolder;
        return this;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Subject subject=SecurityUtils.getSubject();

        String path=request.getRequestURI();

        if(path.equals(logoutUrl)){
            logoutHandler.handle(true,request,response);
            subject.logout();
            return;
        }

        if(path.equals(loginUrl)){
            if(subject.isAuthenticated()){
                logoutHandler.handle(false,request,response);
                subject.logout();
            }
            try{
                attemptAuthentication(request, response);
                subject=SecurityUtils.getSubject();
                loginSuccessHandler.handle(subject,request,response);
            }catch(AuthenticationException e){
                loginFailureHandler.handle(e,request,response);
            }
            return;
        }

        if(enableProcessToken){
            String token=ServletContextUtil.getToken(request,tokenName);
            if(token!=null && !"".equals(token)){
                CustomerAuthToken authToken=new CustomerAuthToken(token);
                try{
                    subject.login(authToken);
                    tokenHolder.refreshToken(token);
                }catch(AuthenticationException e){
                    loginFailureHandler.handle(e,request,response);
                    return;
                }
            }
        }
        chain.doFilter(request,response);
    }

    protected void attemptAuthentication(HttpServletRequest request, HttpServletResponse response){
        logger.info("ShiroTokenFilter try login...");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = null;
            String password = null;

            String contentType=request.getContentType();
            if(contentType!=null && !"".equals(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)){
                logger.info("ShiroTokenFilter login in post json...");
                // json post 方式提交的登录表单
                try{
                    InputStream is=request.getInputStream();
                    ObjectMapper mapper=new ObjectMapper();
                    Map<String,Object> json=mapper.readValue(is,new TypeReference<Map<String,Object>>(){});
                    username=String.valueOf(json.get(getUsernameParameter()));
                    password=String.valueOf(json.get(getPasswordParameter()));
                }catch(Exception e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }else{
                logger.info("ShiroTokenFilter login in form...");
                // 普通form提交的登录表单
                username = this.obtainUsername(request);
                password = this.obtainPassword(request);
            }

            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            logger.info("ShiroTokenFilter user:"+username);

            if(passwordDecoder!=null){
                logger.info("ShiroTokenFilter login password decoder find.");
                password=passwordDecoder.decode(password);
            }

            Subject subject=SecurityUtils.getSubject();

            UsernamePasswordToken info=new UsernamePasswordToken(username,password);
            subject.login(info);
        }
    }

    public String obtainPassword(HttpServletRequest request) {
        return request.getParameter(this.passwordParameter);
    }

    public String obtainUsername(HttpServletRequest request) {
        return request.getParameter(this.usernameParameter);
    }

    public String getUsernameParameter() {
        return this.usernameParameter;
    }

    public String getPasswordParameter() {
        return this.passwordParameter;
    }
}
