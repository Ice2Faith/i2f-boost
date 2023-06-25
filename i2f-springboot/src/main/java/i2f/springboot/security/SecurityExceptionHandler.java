package i2f.springboot.security;

import i2f.core.std.api.ApiResp;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.web.csrf.CsrfException;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ltb
 * @date 2022/4/23 20:21
 * @desc
 */
@ConditionalOnExpression("${i2f.springboot.config.security.enable-exception-handler:true}")
@ControllerAdvice
@Order(1)
public class SecurityExceptionHandler {

    // 处理权限不足异常
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Object exceptCatch(AccessDeniedException e){
        e.printStackTrace();
        return resolveException(e);
    }

    public ApiResp resolveException(AccessDeniedException e) {
        e.printStackTrace();
        if(e instanceof InvalidCsrfTokenException){
            return ApiResp.error(403,"invalid scrf token.");
        }
        else if(e instanceof MissingCsrfTokenException){
            return ApiResp.error(403,"missing csrf token.");
        }
        else if(e instanceof AuthorizationServiceException){
            return ApiResp.error(403,"auth service error.");
        }
        else if(e instanceof CsrfException){
            return ApiResp.error(403,"csrf error.");
        }
        else if(e instanceof AccessDeniedException){
            return ApiResp.error(403,"security access denied.");
        }
        return ApiResp.error(403,"security access denied.");
    }

}
