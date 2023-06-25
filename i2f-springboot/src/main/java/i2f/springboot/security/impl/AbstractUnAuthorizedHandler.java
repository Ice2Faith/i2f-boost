package i2f.springboot.security.impl;

import i2f.core.network.net.http.HttpStatus;
import i2f.core.std.api.ApiResp;
import i2f.springboot.security.SecurityForwardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

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
public class AbstractUnAuthorizedHandler implements UnAuthorizedHandler{
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException ex) throws IOException, ServletException {
        log.info("--------------unauthorized-----------");
        String requestUri=request.getRequestURI();
        if("/error".equals(requestUri)){
            onNotFoundUrl(HttpStatus.NOT_FOUND,requestUri,request,response,ex);
        }else{
            onUnAuthoried(HttpStatus.UNAUTHORIZED,requestUri,request, response, ex);
        }
    }

    public void onNotFoundUrl(int statusCode,String requestUri,HttpServletRequest request,HttpServletResponse response,AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractUnAuthorizedHandler 404 Not-Found:"+requestUri);
        SecurityForwardUtil.forward(request,response, ApiResp.resp(statusCode,"request resource:"+requestUri+" not found.",null));
    }

    public void onUnAuthoried(int statusCode,String requestUri,HttpServletRequest request,HttpServletResponse response,AuthenticationException ex) throws IOException, ServletException {
        log.info("AbstractUnAuthorizedHandler 401 authorize failure:"+requestUri);
        SecurityForwardUtil.forward(request,response,ApiResp.resp(statusCode,"request resource:"+requestUri+" authorize failure,reject access.",null));
    }

}
