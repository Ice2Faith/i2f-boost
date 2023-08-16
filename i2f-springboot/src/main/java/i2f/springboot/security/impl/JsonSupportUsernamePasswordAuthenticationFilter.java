package i2f.springboot.security.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/7 11:14
 * @desc
 */
public class JsonSupportUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String AUTH_ATTR_KEY_USERNAME = "X-Auth-Username";
    public static final String AUTH_ATTR_KEY_PASSWORD = "X-Auth-Password";

    private LoginPasswordDecoder passwordDecoder;

    private BeforeLoginChecker beforeLoginChecker;

    public JsonSupportUsernamePasswordAuthenticationFilter() {

    }

    /**
     * 这里需要注意，自定义实现 UsernamePasswordAuthenticationFilter 时
     * 用来验证的 AuthenticationManager 是不会被注入的，导致直接Null登录报错
     * AuthenticationSuccessHandler 这个如果不带入进来，也是不会进入你自己的成功处理器的
     * 就算在配置中配置了
     *
     * @param manager
     * @param handler
     */
    public JsonSupportUsernamePasswordAuthenticationFilter(AuthenticationManager manager, AuthenticationSuccessHandler handler) {
        super.setAuthenticationManager(manager);
        super.setAuthenticationSuccessHandler(handler);
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildAuthenticationManager(AuthenticationManager manager) {
        super.setAuthenticationManager(manager);
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildAuthenticationSuccessHandler(AuthenticationSuccessHandler handler) {
        super.setAuthenticationSuccessHandler(handler);
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildAuthenticationFailureHandler(AuthenticationFailureHandler handler) {
        super.setAuthenticationFailureHandler(handler);
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildLoginPath(String path) {
        super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(path, "POST"));
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildParameterUsername(String username) {
        super.setUsernameParameter(username);
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildParameterPassword(String password) {
        super.setPasswordParameter(password);
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildLoginPasswordDecoder(LoginPasswordDecoder decoder) {
        this.passwordDecoder = decoder;
        return this;
    }

    public JsonSupportUsernamePasswordAuthenticationFilter buildBeforeLoginChecker(BeforeLoginChecker checker) {
        this.beforeLoginChecker = checker;
        return this;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        logger.info("JsonSupportUsernamePasswordAuthenticationFilter try login...");
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String username = null;
            String password = null;


            String contentType = request.getContentType();
            if (contentType != null && !"".equals(contentType) && contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                logger.info("JsonSupportUsernamePasswordAuthenticationFilter login in post json...");
                // json post 方式提交的登录表单
                try {
                    InputStream is = request.getInputStream();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> json = mapper.readValue(is, new TypeReference<Map<String, Object>>() {
                    });
                    username = String.valueOf(json.get(getUsernameParameter()));
                    password = String.valueOf(json.get(getPasswordParameter()));
                    request.setAttribute(AUTH_ATTR_KEY_USERNAME, username);
                    request.setAttribute(AUTH_ATTR_KEY_PASSWORD, password);
                    if (beforeLoginChecker != null) {
                        beforeLoginChecker.onJsonLogin(username, password, json, request);
                    }
                } catch (Exception e) {
                    if (e instanceof AuthenticationException) {
                        throw (AuthenticationException) e;
                    } else {
                        throw new BadCredentialsException(e.getMessage(), e);
                    }
                }
            } else {
                logger.info("JsonSupportUsernamePasswordAuthenticationFilter login in form...");
                // 普通form提交的登录表单
                username = this.obtainUsername(request);
                password = this.obtainPassword(request);
                request.setAttribute(AUTH_ATTR_KEY_USERNAME, username);
                request.setAttribute(AUTH_ATTR_KEY_PASSWORD, password);
                try {
                    if (beforeLoginChecker != null) {
                        beforeLoginChecker.onFormLogin(username, password, request);
                    }
                } catch (Exception e) {
                    if (e instanceof AuthenticationException) {
                        throw (AuthenticationException) e;
                    } else {
                        throw new BadCredentialsException(e.getMessage(), e);
                    }
                }
            }

            if (username == null) {
                username = "";
            }

            if (password == null) {
                password = "";
            }

            logger.info("JsonSupportUsernamePasswordAuthenticationFilter user:" + username);

            if (passwordDecoder != null) {
                logger.info("JsonSupportUsernamePasswordAuthenticationFilter login password decoder find.");
                password = passwordDecoder.decode(password);
            }

            username = username.trim();
            UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

}
