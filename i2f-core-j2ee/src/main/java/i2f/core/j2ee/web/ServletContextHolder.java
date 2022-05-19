package i2f.core.j2ee.web;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/5/19 19:20
 * @desc 使用过滤器和ThreadLocal特性，实现线程内部随时能够获取到请求和响应
 * 达到一个Context的目的
 * 因此，此过滤器应该是最优先的
 * 并且过滤器要被注册，否则不会生效
 */
public class ServletContextHolder implements Filter {
    private static volatile ThreadLocal<HttpServletRequest> requestHolder=new ThreadLocal<>();
    private static volatile ThreadLocal<HttpServletResponse> responseHolder=new ThreadLocal<>();


    public static void setContext(HttpServletRequest request,HttpServletResponse response){
        requestHolder.set(request);
        responseHolder.set(response);
    }

    public static void removeContext(){
        requestHolder.remove();
        responseHolder.remove();
    }

    public static HttpServletRequest getRequest(){
        return requestHolder.get();
    }

    public static HttpServletResponse getResponse(){
        return responseHolder.get();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse)servletResponse;

        setContext(request,response);

        try{
            filterChain.doFilter(request,response);
        }catch(Exception e){
            throw e;
        }finally {
            removeContext();
        }
    }

    @Override
    public void destroy() {

    }
}
