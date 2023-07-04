package i2f.springboot.security.impl;

import i2f.core.network.net.http.HttpStatus;
import i2f.core.std.api.ApiResp;
import i2f.springboot.security.SecurityForwardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/7 10:31
 * @desc
 */
@Slf4j
public class AbstractAuthorizeExceptionHandler implements AuthorizeExceptionHandler {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("--------------authorize exception-----------");
        String requestUri=request.getRequestURI();
        if("/error".equals(requestUri)){
            onAuthoriedError(HttpStatus.ERROR, requestUri, request, response, ex);
        }else {
            int statusCode=HttpStatus.FORBIDDEN;
            if(ex instanceof InsufficientAuthenticationException){
                statusCode=HttpStatus.UNAUTHORIZED;
            }
            if(ex instanceof UsernameNotFoundException
            || ex instanceof BadCredentialsException){
                statusCode=HttpStatus.UNAUTHORIZED;
            }
            onAuthoriedFailure(statusCode, requestUri, request, response, ex);
        }
    }

    public void onAuthoriedError(int statusCode, String requestUri, HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractAuthorizeExceptionHandler 500 authorize error:"+requestUri+" : "+ex.getMessage(),ex);
        SecurityForwardUtil.forward(request,response,ApiResp.resp(statusCode,"request resource authorize failure,internal error.",null));
    }

    public void onAuthoriedFailure(int statusCode, String requestUri, HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractAuthorizeExceptionHandler "+statusCode+" authorize failure:"+requestUri+" : "+ex.getMessage(),ex);
        SecurityForwardUtil.forward(request,response,ApiResp.resp(statusCode,"request resource authorize failure,reject access.",null));
    }

}
