package i2f.core.j2ee.firewall.wrapper;


import i2f.core.j2ee.firewall.exception.FirewallException;
import i2f.core.j2ee.firewall.util.FirewallUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.net.URL;
import java.util.Collection;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:04
 * @desc
 */
public class FirewallHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public FirewallHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        preCheckRequest(request);
    }

    public static void preCheckRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        FirewallUtils.assertUrlInject("request uri", requestURI);

        String pathInfo = request.getPathInfo();
        FirewallUtils.assertUrlInject("path info", pathInfo);

        String servletPath = request.getServletPath();
        FirewallUtils.assertUrlInject("servlet path", servletPath);

        String contextPath = request.getContextPath();
        FirewallUtils.assertUrlInject("context path", contextPath);

        String method = request.getMethod();

        try {
            String requestURL = request.getRequestURL().toString();
            URL url = new URL(requestURL);
            String urlPath = url.getPath();
            FirewallUtils.assertUrlInject("url path", urlPath);
        } catch (Exception e) {
            if (e instanceof FirewallException) {
                throw (FirewallException) e;
            }
        }

        String contentType = (String.valueOf(request.getContentType())).toLowerCase();
        try {
            if (contentType.contains("multipart/form-data")) {
                Collection<Part> parts = request.getParts();
                for (Part part : parts) {
                    boolean isFilePart = false;
                    String fileName = "";
                    if (!isFilePart) {
                        String submittedFileName = part.getSubmittedFileName();
                        if (submittedFileName != null && !"".equalsIgnoreCase(submittedFileName)) {
                            isFilePart = true;
                            fileName = submittedFileName;
                        }
                    }
                    if (!isFilePart) {
                        Collection<String> headerNames = part.getHeaderNames();
                        for (String headerName : headerNames) {
                            if ("content-disposition".equalsIgnoreCase(headerName)) {
                                String contentDisposition = part.getHeader(headerName);
                                fileName = FirewallUtils.parseContentDispositionFileName(contentDisposition);
                                if (fileName != null && !"".equals(fileName)) {
                                    isFilePart = true;
                                }
                            }
                            if (isFilePart) {
                                break;
                            }
                        }
                    }

                    if (!isFilePart) {
                        continue;
                    }
                    FirewallUtils.assertUrlInject("part filename", fileName);
                }
            }
        } catch (Exception e) {
            if (e instanceof FirewallException) {
                throw (FirewallException) e;
            }
        }

    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        FirewallUtils.assertUrlInject("request dispatcher", path);
        return super.getRequestDispatcher(path);
    }

}
