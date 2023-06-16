package i2f.springboot.secure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import i2f.core.j2ee.web.HttpServletRequestProxyWrapper;
import i2f.core.j2ee.web.HttpServletResponseProxyWrapper;
import i2f.core.thread.NamingThreadFactory;
import i2f.secure.StackTraceUtils;
import i2f.spring.mapping.MappingUtil;
import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.consts.SecureConsts;
import i2f.springboot.secure.consts.SecureErrorCode;
import i2f.springboot.secure.data.SecureCtrl;
import i2f.springboot.secure.data.SecureHeader;
import i2f.springboot.secure.exception.SecureException;
import i2f.springboot.secure.util.RequestUtils;
import i2f.springboot.secure.util.SecureUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author ltb
 * @date 2022/6/29 13:59
 * @desc RSA+AES加密的核心过滤器
 */
@Slf4j
@Component
@Order(-1)
@WebFilter(
        urlPatterns = "/**",
        dispatcherTypes = {
                DispatcherType.REQUEST,
                DispatcherType.FORWARD
        }
)
public class SecureTransferFilter implements Filter, InitializingBean, ApplicationContextAware {

    @Override
    public void afterPropertiesSet() {
        log.info("SecureTransferFilter config done.");
    }

    @Autowired
    private SecureTransfer secureTransfer;

    @Autowired
    private SecureConfig secureConfig;

    @Autowired
    private MappingUtil mappingUtil;

    private ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private ConcurrentMap<String, Long> nonceCache = new ConcurrentHashMap<>();
    private ScheduledExecutorService pool = Executors.newScheduledThreadPool(30, new NamingThreadFactory("secure", "nonce"));

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);

        Map<String, MappingJackson2HttpMessageConverter> beans = context.getBeansOfType(MappingJackson2HttpMessageConverter.class, true, true);


        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        doFilterInternal(httpRequest, httpResponse, filterChain);

    }

    public boolean isEncUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String encUrl = SecureUtils.combinePath(contextPath, secureConfig.getEncUrlPath(), "/");
        if (!encUrl.startsWith("/")) {
            encUrl = "/" + encUrl;
        }
        if (!encUrl.endsWith("/")) {
            encUrl = encUrl + "/";
        }
        if (!uri.startsWith("/")) {
            uri = "/" + uri;
        }
        boolean isEnc = uri.startsWith(encUrl);
        return isEnc;
    }

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        if (request instanceof MultipartHttpServletRequest) {
            chain.doFilter(request, response);
            return;
        }

        String contentType = request.getContentType();
        if (contentType != null) {
            contentType = contentType.toLowerCase();
        }
        if ("multipart/form-data".equals(contentType)) {
            chain.doFilter(request, response);
            return;
        }

        if (!StringUtils.isEmpty(secureConfig.getResponseCharset())) {
            response.setCharacterEncoding(secureConfig.getResponseCharset());
        }
        String requestUrl = request.getRequestURI();
        log.debug("enter filter:" + requestUrl);
        // 尝试获取方法与安全注解
        SecureCtrl ctrl = SecureUtils.parseSecureCtrl(request, secureConfig, mappingUtil);


        boolean wrapEncResp = isEncUrl(request);

        if (!ctrl.in && !ctrl.out && !wrapEncResp) {
            log.debug("jump white url:" + request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest nextRequest = request;
        HttpServletResponse nextResponse = response;


        // 包装响应，使得能够再次消费，以进行加密处理
        if (ctrl.out || wrapEncResp) {
            HttpServletResponseProxyWrapper responseProxyWrapper = new HttpServletResponseProxyWrapper(response);
            nextResponse = responseProxyWrapper;
        }

        String decryped = (String) request.getAttribute(SecureConsts.FILTER_DECRYPT_HEADER);
        SecureHeader requestHeader = null;
        if (ctrl.in && !SecureConsts.FLAG_ENABLE.equals(decryped)) {
            // 包装请求，使得能够再次消费，以进行解密处理
            HttpServletRequestProxyWrapper requestProxyWrapper = new HttpServletRequestProxyWrapper(request);
            nextRequest = requestProxyWrapper;

            try {
                requestHeader = SecureUtils.parseSecureHeader(secureConfig.getHeaderName(), secureConfig.getHeaderSeparator(), request);

                // 如果是一次性头，并且一次性签名头都存在，则可以限制非正常的重放请求
                // 正常的请求是客户端发起的，每一次一次性头都是不同的
                // 然而，非正常的请求，可以进行重放请求达到目的
                // 这样的请求，就会出现一次性头重复，存在相同的签名
                String clientIp = RequestUtils.getIp(request);
                // 对于服务端而言，nonce从客户端发送过来，很难避免不同的客户端发送过来的nonce重复的问题
                // 如果不进行客户端隔离，就会导致不少正常的请求被拦截为重放
                // 因此需要结合客户端IP判定nonce
                String cacheNonce = clientIp + ":" + requestHeader.nonce;
                if (nonceCache.containsKey(cacheNonce)) {
                    throw new SecureException(SecureErrorCode.BAD_NONCE, "不允许重放请求");
                }

                nonceCache.put(cacheNonce, 1L);

                // 这里定时消除一个时间窗口内的重放请求
                // 更好的实现方式时使用redis，避免一个时间窗口内出现大量请求
                // 导致map和pool宕机
                pool.schedule(new Runnable() {
                    @Override
                    public void run() {
                        nonceCache.remove(cacheNonce);
                    }
                }, secureConfig.getNonceTimeoutSeconds(), TimeUnit.SECONDS);


                byte[] bytes = requestProxyWrapper.getBodyBytes();
                String srcText = null;
                String srcSswp=null;

                srcSswp=request.getParameter(secureConfig.getParameterName());

                // 如果有请求体，解密请求体
                if (bytes.length > 0) {
                    srcText = new String(bytes, request.getCharacterEncoding());
                }

                String signText="";
                if(srcText!=null){
                    signText+=srcText;
                }
                if(srcSswp!=null){
                    signText+=srcSswp;
                }
                boolean ok = SecureUtils.verifySecureHeader(signText, requestHeader);
                if (!ok) {
                    throw new SecureException(SecureErrorCode.BAD_SIGN, "签名验证失败");
                }

                String aesKey = secureTransfer.getRequestSecureHeader(requestHeader.randomKey, requestHeader.rsaSign);
                if (aesKey == null) {
                    throw new SecureException(SecureErrorCode.BAD_RANDOM_KEY, "随机秘钥无效或已失效，请重试！");
                }
                String replaceQueryString=null;
                Map<String,List<String>> replaceParameterMap=null;
                if(!StringUtils.isEmpty(srcSswp)){
                    String json=secureTransfer.decrypt(srcSswp,aesKey);
                    String ps=new ObjectMapper().readValue(json,String.class);
                    String[] arr=ps.split("&");
                    Map<String, List<String>> pmap=new HashMap<>();
                    for(int i=0;i<arr.length;i++){
                        String item=arr[i];
                        String[] pair=item.split("=",2);
                        String pk=pair[0];
                        String pv="";
                        if(pair.length>1){
                            pv=pair[1];
                        }
                        if(!pmap.containsKey(pk)){
                            pmap.put(pk,new ArrayList<>());
                        }
                        pv= URLDecoder.decode(pv,"UTF-8");
                        pmap.get(pk).add(pv);
                    }
                    replaceQueryString=ps;
                    replaceParameterMap=pmap;
                }
                if (srcText != null) {
                    log.debug("src body:" + srcText);
                    String decryptText = secureTransfer.decrypt(srcText, aesKey);
                    log.debug("decrypt body:" + decryptText);
                    // 将解密的数据重新包装
                    requestProxyWrapper = new HttpServletRequestProxyWrapper(request, decryptText.getBytes(request.getCharacterEncoding()));
                }
                if(replaceQueryString!=null){
                    requestProxyWrapper.setQueryString(replaceQueryString);
                }
                if(replaceParameterMap!=null){
                    requestProxyWrapper.setParameterMap(replaceParameterMap);
                }

            } catch (Throwable e) {
                log.error(e.getClass().getName(), e);
                // 标记异常头，如果发生异常，标记后在AOP或者RequestAdvice中抛出异常，以进行ExceptionHandler处理
                requestProxyWrapper.setAttribute(SecureConsts.FILTER_EXCEPTION_ATTR_KEY, e);
            }
            log.debug("mark as decrypted.");
            requestProxyWrapper.setAttribute(SecureConsts.SECURE_HEADER_ATTR_KEY, requestHeader);
            // 标记已被解密
            requestProxyWrapper.setAttribute(SecureConsts.FILTER_DECRYPT_HEADER, SecureConsts.FLAG_ENABLE);

            nextRequest = requestProxyWrapper;

        }


        log.debug("chain next...");

        log.debug("next request:" + nextRequest.getClass().getName());
        log.debug("next response:" + nextResponse.getClass().getName());

        // 开始进入过滤器
        chain.doFilter(nextRequest, nextResponse);

        log.info("filter trace:\n" + StackTraceUtils.getCurrentStackTrace());

        if (!ctrl.out && !wrapEncResp) {
            log.debug("not require secure response.");
            return;
        }

        HttpServletResponseProxyWrapper responseProxyWrapper = (HttpServletResponseProxyWrapper) nextResponse;

        // 加密响应体
        byte[] edata = responseProxyWrapper.getBodyBytes();

        String requireResp = (String) request.getAttribute(SecureConsts.SECURE_REQUIRE_RESPONSE);
        String encryped = (String) request.getAttribute(SecureConsts.FILTER_ENCRYPT_HEADER);
        // 只有响应头包含加密头时，才加密请求
        if ((ctrl.out || SecureConsts.FLAG_ENABLE.equals(requireResp))
                && edata.length > 0
                && !SecureConsts.FLAG_ENABLE.equals(encryped)) {
            log.debug("find response secure.");

            if (!StringUtils.isEmpty(secureConfig.getResponseCharset())) {
                log.debug("reset response charset as:" + secureConfig.getResponseCharset());
                // 重置字符编码
                nextResponse.setCharacterEncoding(secureConfig.getResponseCharset());
            }

            // 每次生成随机aes加密秘钥
            String aesKey = secureTransfer.aesKeyGen();

            SecureHeader responseHeader = new SecureHeader();
            responseHeader.randomKey = secureTransfer.getResponseSecureHeader(aesKey);
            responseHeader.rsaSign = secureTransfer.getRsaSign();
            responseHeader.nonce = secureTransfer.makeNonce();

            String enData = null;

            log.debug("encrypt response body...");

            // 特殊处理返回值为string类型的情况
            String strHeader = responseProxyWrapper.getHeader(SecureConsts.STRING_RETURN_HEADER);
            if (SecureConsts.FLAG_ENABLE.equals(strHeader)) {
                String retStr = new String(edata, responseProxyWrapper.getCharacterEncoding());
                enData = secureTransfer.encrypt(retStr, aesKey);
            } else {
                // 使用AES秘钥加密响应体
                enData = secureTransfer.encryptJsonBytes(edata, aesKey);
            }

            responseHeader.sign = SecureUtils.makeSecureSign(enData, responseHeader);

            // 写回数据体
            edata = enData.getBytes(responseProxyWrapper.getCharacterEncoding());

            log.debug("rewrite content-type/content-length");

            // 重置响应类型
            responseProxyWrapper.setContentType("text/plain");

            // 重置响应长度
            response.setContentLengthLong(enData.length());

            String header = SecureUtils.encodeSecureHeader(responseHeader, secureConfig.getHeaderSeparator());
            response.setHeader(secureConfig.getHeaderName(), header);
            if (requestHeader != null) {
                if (!responseHeader.rsaSign.equals(requestHeader.rsaSign)) {
                    response.setHeader(SecureConsts.SECURE_DYNAMIC_KEY_HEADER, secureTransfer.getWebRsaPublicKey());
                }
            }
            secureTransfer.setExposeHeader(response);

            request.setAttribute(SecureConsts.FILTER_ENCRYPT_HEADER, SecureConsts.FLAG_ENABLE);
        }

        log.debug("write response and finish...");

        // 响应数据
        response.setContentType(responseProxyWrapper.getContentType());
        response.setCharacterEncoding(responseProxyWrapper.getCharacterEncoding());
        OutputStream os = response.getOutputStream();
        os.write(edata);
        os.flush();

        log.debug("leave filter.");
    }
}
