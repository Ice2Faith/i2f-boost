package i2f.springboot.shiro.def;

import i2f.core.api.ApiResp;
import i2f.springboot.security.SecurityForwardUtil;
import i2f.springboot.shiro.IShiroUser;
import i2f.springboot.shiro.ShiroConfig;
import i2f.springboot.shiro.handler.ILoginSuccessHandler;
import i2f.springboot.shiro.token.AbstractShiroTokenHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @author ltb
 * @date 2022/4/23 17:46
 * @desc
 */
@ConditionalOnBean(ShiroConfig.class)
@ConditionalOnMissingBean(ILoginSuccessHandler.class)
@Component
@Slf4j
@Data
public class DefaultLoginSuccessHandler implements ILoginSuccessHandler, InitializingBean {

    @Autowired
    private AbstractShiroTokenHolder tokenHolder;

    private boolean enableSingleLogin=true;

    @Override
    public void handle(Subject subject, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        IShiroUser user=(IShiroUser)subject.getPrincipal();
        String token= UUID.randomUUID().toString().replaceAll("-","");
        if(enableSingleLogin){
            tokenHolder.setSingleToken(user.getUsername(),token,user);
        }else{
            tokenHolder.setToken(token,user);
        }
        SecurityForwardUtil.forward(request,response, ApiResp.success(token));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("DefaultLoginSuccessHandler config done.");
    }
}
