package i2f.springboot.shiro.def;

import i2f.core.std.api.ApiResp;
import i2f.springboot.security.SecurityForwardUtil;
import i2f.springboot.shiro.ShiroConfig;
import i2f.springboot.shiro.handler.ILoginFailureHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/23 17:43
 * @desc
 */
@ConditionalOnBean(ShiroConfig.class)
@ConditionalOnMissingBean(ILoginFailureHandler.class)
@Component
@Slf4j
public class DefaultLoginFailureHandler implements ILoginFailureHandler, InitializingBean {
    @Override
    public void handle(AuthenticationException ex, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        SecurityForwardUtil.forward(request,response, ApiResp.error(401,ex.getMessage()));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultLoginFailureHandler config done.");
    }
}

