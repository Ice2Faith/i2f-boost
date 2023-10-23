package i2f.spring.proxy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/10/23 8:51
 * @desc
 */
public class HttpProxyFilter implements Filter {
    public static final String FILTER_KEY = HttpProxyFilter.class.getName();
    public static final String FILTER_VALUE = "true";

    private HttpProxyHandler httpProxyHandler;

    public HttpProxyFilter(HttpProxyHandler httpProxyHandler) {
        this.httpProxyHandler = httpProxyHandler;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        Object key = servletRequest.getAttribute(FILTER_KEY);
        if (FILTER_VALUE.equals(key)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!(servletRequest instanceof HttpServletRequest)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        if (!(servletResponse instanceof HttpServletResponse)) {
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        servletRequest.setAttribute(FILTER_KEY, FILTER_VALUE);

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        doInnerFilter(request, response, filterChain);
    }

    public void doInnerFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (httpProxyHandler == null) {
            chain.doFilter(request, response);
            return;
        }

        try {
            boolean ok = httpProxyHandler.handle(request, response);
            if (ok) {
                return;
            }
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }

        chain.doFilter(request, response);
    }
}
