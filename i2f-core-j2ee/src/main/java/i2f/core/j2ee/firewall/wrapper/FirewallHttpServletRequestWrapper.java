package i2f.core.j2ee.firewall.wrapper;


import i2f.core.j2ee.firewall.context.FirewallContext;
import i2f.core.j2ee.firewall.exception.FirewallException;
import i2f.core.j2ee.firewall.util.FirewallUtils;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.Part;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;

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
        if (FirewallContext.enableUrl) {
            assertHttpServletUrl(request);
        }
        if (FirewallContext.enableMethod) {
            assertHttpServletMethod(request);
        }
        if (FirewallContext.enableMultipart) {
            assertHttpServletMultipart(request);
        }
        if (FirewallContext.enableParameter) {
            assertHttpServletParameters(request);
        }
        if (FirewallContext.enableQueryString) {
            assertHttpSevletQueryString(request);
        }
    }

    public static void assertHttpSevletQueryString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        if (queryString != null && !"".equals(queryString)) {
            String[] arr = queryString.split("&");
            for (String pair : arr) {
                String[] kv = pair.split("=", 2);
                if (kv.length == 2) {
                    FirewallUtils.assertPossiblePathParameter("query path", kv[1]);
                }
            }
        }
    }

    public static void assertHttpServletParameters(HttpServletRequest request) {
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            String[] parameterValues = request.getParameterValues(paramName);
            for (String parameterValue : parameterValues) {
                FirewallUtils.assertPossiblePathParameter("param path", parameterValue);
            }
        }
    }

    public static void assertHttpServletMultipart(HttpServletRequest request) {
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
                    FirewallUtils.assertUrlInject("part filename", fileName, true);
                }
            }
        } catch (Exception e) {
            if (e instanceof FirewallException) {
                throw (FirewallException) e;
            }
        }
    }

    public static void assertHttpServletMethod(HttpServletRequest request) {
        String method = request.getMethod();
        FirewallUtils.assertHttpMethod("http method", method);
    }

    public static void assertHttpServletUrl(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        FirewallUtils.assertUrlInject("request uri", requestURI, false);

        String pathInfo = request.getPathInfo();
        FirewallUtils.assertUrlInject("path info", pathInfo, false);

        String servletPath = request.getServletPath();
        FirewallUtils.assertUrlInject("servlet path", servletPath, false);

        String contextPath = request.getContextPath();
        FirewallUtils.assertUrlInject("context path", contextPath, false);

        try {
            String requestURL = request.getRequestURL().toString();
            URL url = new URL(requestURL);
            String urlPath = url.getPath();
            FirewallUtils.assertUrlInject("url path", urlPath, false);
        } catch (Exception e) {
            if (e instanceof FirewallException) {
                throw (FirewallException) e;
            }
        }
    }


    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        FirewallUtils.assertUrlInject("request dispatcher", path, false);
        return super.getRequestDispatcher(path);
    }

}
