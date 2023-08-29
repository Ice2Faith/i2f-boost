package i2f.core.j2ee.firewall.wrapper;


import i2f.core.j2ee.firewall.util.FirewallUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:04
 * @desc
 */
public class FirewallHttpServletResponseWrapper extends HttpServletResponseWrapper {
    public FirewallHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void addCookie(Cookie cookie) {
        FirewallUtils.assertCrLfXssInject("cookie name", cookie.getName());
        FirewallUtils.assertCrLfXssInject("cookie value", cookie.getValue());
        FirewallUtils.assertCrLfXssInject("cookie path", cookie.getPath());
        FirewallUtils.assertCrLfXssInject("cookie domain", cookie.getDomain());
        FirewallUtils.assertCrLfXssInject("cookie comment", cookie.getComment());
        FirewallUtils.assertHeaderBadChars("cookie name", cookie.getName());
        FirewallUtils.assertHeaderBadChars("cookie value", cookie.getValue());
        FirewallUtils.assertHeaderBadChars("cookie path", cookie.getPath());
        FirewallUtils.assertHeaderBadChars("cookie domain", cookie.getDomain());
        FirewallUtils.assertHeaderBadChars("cookie comment", cookie.getComment());

        super.addCookie(cookie);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
        FirewallUtils.assertCrLfXssInject("redirect location", location);
        FirewallUtils.assertHeaderBadChars("redirect location", location);
        super.sendRedirect(location);
    }

    @Override
    public void setHeader(String name, String value) {
        FirewallUtils.assertCrLfXssInject("header name", name);
        FirewallUtils.assertCrLfXssInject("header value", value);
        FirewallUtils.assertContentDispositionHeader(name, value);
        FirewallUtils.assertHeaderBadChars("header name", name);
        FirewallUtils.assertHeaderBadChars("header value", value);
        super.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        FirewallUtils.assertCrLfXssInject("header name", name);
        FirewallUtils.assertCrLfXssInject("header value", value);
        FirewallUtils.assertContentDispositionHeader(name, value);
        FirewallUtils.assertHeaderBadChars("header name", name);
        FirewallUtils.assertHeaderBadChars("header value", value);
        super.addHeader(name, value);
    }

    @Override
    public void setContentType(String type) {
        FirewallUtils.assertCrLfXssInject("content-type", type);
        FirewallUtils.assertHeaderBadChars("content-type", type);
        super.setContentType(type);
    }


}
