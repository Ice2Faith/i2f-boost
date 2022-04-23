package i2f.springboot.shiro.def;

import i2f.core.api.ApiResp;
import i2f.core.j2ee.web.ServletContextUtil;
import i2f.springboot.security.SecurityForwardUtil;
import i2f.springboot.shiro.handler.ILogoutHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/23 17:51
 * @desc
 */
@ConditionalOnMissingBean(ILogoutHandler.class)
@Component
@Data
@NoArgsConstructor
@Slf4j
public class DefaultLogoutHandler implements ILogoutHandler, InitializingBean {
    private String tokenName="token";
    @Override
    public void handle(boolean fullMode,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token= ServletContextUtil.getToken(request,tokenName);
        if(token!=null && !"".equals(token)){
            DefaultCustomerTokenRealm.onlineUser.remove(token);
        }
        if(fullMode){
            SecurityForwardUtil.forward(request,response, ApiResp.success("ok"));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultLogoutHandler config done.");
    }
}
