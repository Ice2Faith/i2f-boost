package i2f.springboot.security.def.token;

import i2f.core.network.net.http.HttpStatus;
import i2f.core.std.api.ApiResp;
import i2f.extension.guarder.LoginGuarder;
import i2f.springboot.security.SecurityForwardUtil;
import i2f.springboot.security.exception.BoostAuthenticationException;
import i2f.springboot.security.impl.JsonSupportUsernamePasswordAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/7/4 11:37
 * @desc
 */
@ConditionalOnMissingBean(AuthenticationFailureHandler.class)
@Slf4j
@Component
public class DefaultAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired(required = false)
    protected LoginGuarder loginGuarder;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("--------------unauthorized-----------");
        String requestUri = request.getRequestURI();
        onUnAuthoried(HttpStatus.UNAUTHORIZED, requestUri, request, response, ex);
    }

    public void onUnAuthoried(int statusCode, String requestUri, HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("DefaultAuthenticationFailureHandler 401 authorize failure:" + requestUri + " : " + ex.getMessage(), ex);
        if (loginGuarder != null) {
            String username = (String) request.getAttribute(JsonSupportUsernamePasswordAuthenticationFilter.AUTH_ATTR_KEY_USERNAME);
            if (!StringUtils.isEmpty(username)) {
                loginGuarder.failure(request, username);
            }
        }

        String msg = "request resource authorize failure,reject access.";
        if (ex instanceof BoostAuthenticationException) {
            msg = ex.getMessage();
        }
        SecurityForwardUtil.forward(request, response, ex, ApiResp.resp(statusCode, msg, null));
    }
}
