package i2f.springboot.shiro.filter;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.springboot.shiro.token.CustomerAuthToken;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/21 18:38
 * @desc
 */
@Data
@NoArgsConstructor
public class ShiroTokenFilter extends OncePerRequestFilter {
    private String tokenName="token";
    public ShiroTokenFilter(String tokenName){
        this.tokenName=tokenName;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        Subject subject=SecurityUtils.getSubject();

        if(!subject.isAuthenticated()){
            String token=ServletContextUtil.getToken(request,tokenName);
            if(token!=null && !"".equals(token)){
                CustomerAuthToken authToken=new CustomerAuthToken(token);
                subject.login(authToken);
            }
        }
        chain.doFilter(request,response);
    }
}
