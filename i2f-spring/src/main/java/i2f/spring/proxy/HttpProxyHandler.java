package i2f.spring.proxy;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/6/9 17:00
 * @desc
 */
public class HttpProxyHandler {
    public static HttpProxyHandler build(){
        return new HttpProxyHandler();
    }
    private ConcurrentHashMap<String, String> pathMapping=new ConcurrentHashMap<>();
    public HttpProxyHandler(){

    }
    public HttpProxyHandler mapping(String proxyPrefix,String targetUrl){
        pathMapping.put(proxyPrefix,targetUrl);
        return this;
    }
    /**
     * 请求的完美转发
     * 举例：
     * 代理这样的请求：
     * http://localhost:8001/proxy/svc-api/search/all
     * 转发到：
     * http://192.168.1.20:6633/svc-api/search/all
     * 那么入参
     * proxyPrefix=/proxy
     * targetUrl=http://192.168.1.20:6633
     * @param request 原始请求
     * @param response 原始响应
     * @param proxyPrefix 跳转的前缀，一般代理来说都是一个前缀+目标的path
     * @param targetUrl 目标基本路径
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void proxy(HttpServletRequest request, HttpServletResponse response,String proxyPrefix,String targetUrl) throws IOException, URISyntaxException {
        URI uri = new URI(request.getRequestURI());
        String path = uri.getPath();
        String contextPath = request.getContextPath();
        if (!StringUtils.isEmpty(contextPath)) {
            if (path.startsWith(contextPath)) {
                path = path.substring(contextPath.length());
            }
        }
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        String query = request.getQueryString();
        String target = targetUrl + path.replace(proxyPrefix, "");
        if (query != null && !"".equals(query) && !"null".equals(query)) {
            target = target + "?" + query;
        }
        URI newUri = new URI(target);
        // 执行代理查询
        String methodName = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(methodName);
        if (httpMethod == null) {
            return;
        }
        ClientHttpRequest delegate = new SimpleClientHttpRequestFactory().createRequest(newUri, httpMethod);
        Enumeration<String> headerNames = request.getHeaderNames();
        // 设置请求头
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            Enumeration<String> v = request.getHeaders(headerName);
            List<String> arr = new ArrayList<>();
            while (v.hasMoreElements()) {
                arr.add(v.nextElement());
            }
            delegate.getHeaders().addAll(headerName, arr);
        }
        StreamUtils.copy(request.getInputStream(), delegate.getBody());
        // 执行远程调用
        ClientHttpResponse clientHttpResponse = delegate.execute();
        response.setStatus(clientHttpResponse.getStatusCode().value());
        // 设置响应头
        clientHttpResponse.getHeaders().forEach((key, value) -> value.forEach(it -> {
            response.setHeader(key, it);
        }));
        StreamUtils.copy(clientHttpResponse.getBody(), response.getOutputStream());
    }

    public boolean handle(HttpServletRequest request, HttpServletResponse response) throws IOException, URISyntaxException {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (!StringUtils.isEmpty(contextPath)) {
            if (uri.startsWith(contextPath)) {
                uri = uri.substring(contextPath.length());
            }
        }
        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        String targetUrl = null;
        String proxyPrefix = null;
        for (String prefix : pathMapping.keySet()) {
            if (uri.startsWith(prefix)) {
                proxyPrefix = prefix;
                targetUrl = pathMapping.get(prefix);
                break;
            }
        }
        if (proxyPrefix != null && targetUrl != null) {
            proxy(request, response, proxyPrefix, targetUrl);
            return true;
        }
        return false;
    }

}
