package i2f.springboot.security.impl.token;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/7 9:38
 * @desc
 */
@Slf4j
public abstract class AbstractAuthenticationTokenFilter extends AuthenticationTokenFilter {

    @Override
    protected Authentication getTokenAuthentication(HttpServletRequest request, HttpServletResponse response)  throws ServletException, IOException {
        log.info("AbstractAuthenticationTokenFilter get token auth.");
        String tokenName=getTokenName();
        log.info("AbstractAuthenticationTokenFilter tokenName:"+tokenName);
        String token=getToken(request,tokenName);
        if(token==null || "".equals(token)){
            return null;
        }
        log.info("AbstractAuthenticationTokenFilter token :"+token);
        UserDetails user=getUserDetailByToken(token,request,response);
        if(user==null){
            return null;
        }
        request.setAttribute("token",token);
        UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return auth;
    }

    protected  String getTokenName(){
        return "token";
    }

    protected abstract UserDetails getUserDetailByToken(String token,HttpServletRequest request, HttpServletResponse response);

    public static String getToken(HttpServletRequest request,String tokenName){
        String token=request.getHeader(tokenName);
        log.info("AbstractAuthenticationTokenFilter token in header:"+token);
        if(token==null || "".equals(token)){
            token=request.getParameter(tokenName);
            log.info("AbstractAuthenticationTokenFilter token in parameter:"+token);
        }
        return token;
    }
}
