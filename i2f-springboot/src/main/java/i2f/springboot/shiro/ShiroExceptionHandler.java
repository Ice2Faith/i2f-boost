package i2f.springboot.shiro;

import i2f.core.api.ApiResp;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.HostUnauthorizedException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ltb
 * @date 2022/4/23 20:21
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.shiro.enable-exception-handler:true}")
@ControllerAdvice
@Order(1)
public class ShiroExceptionHandler {

    @ExceptionHandler(ShiroException.class)
    @ResponseBody
    public Object exceptCatch(ShiroException e){
        e.printStackTrace();
        return resolveException(e);
    }

    public ApiResp resolveException(ShiroException e) {
        e.printStackTrace();
        if(e instanceof HostUnauthorizedException){
            return ApiResp.error(403,"host not permission.");
        }
        else if(e instanceof UnauthorizedException){
            return ApiResp.error(403,"not permission.");
        }
        else if(e instanceof UnauthenticatedException){
            return ApiResp.error(401,"not login.");
        }
        else if(e instanceof AuthorizationException){
            return ApiResp.error(401,"login failure.");
        }
        else if(e instanceof ShiroException){
            return ApiResp.error(500,"shiro error.");
        }
        return ApiResp.error(500,"shiro error.");
    }
}
