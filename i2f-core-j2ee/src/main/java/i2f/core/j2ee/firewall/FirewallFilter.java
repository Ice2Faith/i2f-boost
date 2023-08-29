package i2f.core.j2ee.firewall;

import i2f.core.j2ee.firewall.exception.FirewallException;
import i2f.core.j2ee.firewall.wrapper.FirewallHttpServletRequestWrapper;
import i2f.core.j2ee.firewall.wrapper.FirewallHttpServletResponseWrapper;
import i2f.core.network.net.http.HttpStatus;
import lombok.Data;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:02
 * @desc
 */
@Data
@WebFilter(
        urlPatterns = "/**",
        dispatcherTypes = {DispatcherType.REQUEST}
)
public class FirewallFilter implements Filter {
    public static final String FIREWALL_FILTER_ATTR_KEY = "firewall-filter";
    public static final String FIREWALL_FLAG_ENABLE = "true";


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 仅针对HttpServletRequest进行
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        // 检验是否已经经过防火墙
        Object attr = request.getAttribute(FIREWALL_FILTER_ATTR_KEY);
        if (attr != null) {
            String val = String.valueOf(attr);
            if (FIREWALL_FLAG_ENABLE.equals(val)) {
                chain.doFilter(request, response);
                return;
            }
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        try {
            doInnerFilter(httpRequest, httpResponse, chain);
        } catch (FirewallException e) {
            httpResponse.setStatus(HttpStatus.BAD_REQUEST);
            httpResponse.setContentType("text/plain;charset=UTF-8");
            httpResponse.getWriter().write(e.getMessage());
        }
    }

    public void doInnerFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setAttribute(FIREWALL_FILTER_ATTR_KEY, FIREWALL_FLAG_ENABLE);
        FirewallHttpServletRequestWrapper requestWrapper = new FirewallHttpServletRequestWrapper(request);
        FirewallHttpServletResponseWrapper responseWrapper = new FirewallHttpServletResponseWrapper(response);
        chain.doFilter(requestWrapper, responseWrapper);
    }
}
