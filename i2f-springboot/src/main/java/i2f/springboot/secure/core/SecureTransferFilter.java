package i2f.springboot.secure.core;

import i2f.springboot.secure.SecureConfig;
import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.servlet.HttpServletRequestProxyWrapper;
import i2f.springboot.secure.servlet.HttpServletResponseProxyWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/6/29 13:59
 * @desc RSA+AES加密的核心过滤器
 */
@Slf4j
@Component
@Order(1)
@WebFilter(urlPatterns = "/**")
public class SecureTransferFilter extends OncePerRequestFilter implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        log.info("SecureTransferFilter config done.");
    }

    @Autowired
    private SecureTransfer secureTransfer;

    private static AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private SecureConfig secureConfig;

    @Autowired
    private FastRequestMappingProvider requestMappingProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.debug("enter filter:" + request.getRequestURI());
        // 尝试获取方法与安全注解
        Method method = requestMappingProvider.getMatchRequestMethod(request);
        SecureParams ann = null;
        if (method != null) {
            log.debug("find mapping handler:" + method.getDeclaringClass().getName() + "." + method.getName());
            ann = SecureTransferAop.getMethodAnnotation(method);
            if(ann==null){
                log.debug("not any secure annotation found, jump");
                chain.doFilter(request,response);
                return;
            }
            if(!ann.in() && !ann.out()){
                log.debug("request and response secure all not open, jump");
                chain.doFilter(request,response);
                return;
            }
        }

        // 是否白名单，白名单则忽略
        String whitePaths = secureConfig.getWhiteAntPaths();
        boolean isWhite = false;
        if (whitePaths != null) {
            String[] pattens = whitePaths.split(",");
            String requestUrl = request.getRequestURI();
            for (String item : pattens) {
                if (antPathMatcher.match(item, requestUrl)) {
                    isWhite = true;
                    break;
                }
            }
        }

        // 如果在白名单内，直接忽略
        if (isWhite) {
            log.debug("jump white url:" + request.getRequestURI());
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest nextRequest = request;
        HttpServletResponse nextResponse = response;

        // 包装响应，使得能够再次消费，以进行加密处理
        if (ann != null && ann.out()) {
            HttpServletResponseProxyWrapper responseProxyWrapper = new HttpServletResponseProxyWrapper(response);
            nextResponse = responseProxyWrapper;
        }

        if (ann != null && ann.in()) {
            String aesKeyTransfer = request.getHeader(SecureTransfer.SECURE_DATA_HEADER);
            boolean isEncrypt = aesKeyTransfer != null && !"".equals(aesKeyTransfer);
            // 有加密则进行解密
            if (isEncrypt) {
                // 包装请求，使得能够再次消费，以进行解密处理
                HttpServletRequestProxyWrapper requestProxyWrapper = new HttpServletRequestProxyWrapper(request);

                log.debug("find request secure.");
                byte[] bytes = requestProxyWrapper.getBodyBytes();

                String signPassHeader=null;
                String signNoncePassHeader=null;

                // 如果有请求体，解密请求体
                if (bytes.length > 0) {
                    log.debug("decrypt request body...");
                    String aesKey = secureTransfer.getRequestSecureHeader(request);
                    String srcText = new String(bytes, request.getCharacterEncoding());

                    // 如果有消息签名，则验证消息签名
                    String signHeader = request.getHeader(SecureTransfer.SECURE_SIGN_HEADER);
                    if(signHeader!= null && !"".equals(signHeader)){
                        String signText=srcText;


                        // 计算签名，并给出比对结果到请求头中，交给接下去的AOP中使用
                        String sign=StringSignature.sign(signText);


                        log.info("signHeader:"+signHeader);
                        log.info("signCalc:"+sign);

                        if(sign.equalsIgnoreCase(signHeader)){
                            signPassHeader=SecureTransfer.FLAG_ENABLE;
                        }else{
                            signPassHeader=SecureTransfer.FLAG_DISABLE;
                        }

                        // 如果存在一次性消息头，需要结合一次性消息头一起计算签名
                        String nonceHeader=request.getHeader(SecureTransfer.SECURE_NONCE_HEADER);
                        String signNonceHeader=request.getHeader(SecureTransfer.SECURE_SIGN_NONCE_HEADER);
                        log.info("nonceHeader:"+nonceHeader);
                        if(nonceHeader!=null &&!"".equals(nonceHeader)){
                            String signNonceText=nonceHeader+sign;
                            String signNonce=StringSignature.sign(signNonceText);
                            if(signNonce.equalsIgnoreCase(signNonceHeader)){
                                signNoncePassHeader=SecureTransfer.FLAG_ENABLE;
                            }else{
                                signNoncePassHeader=SecureTransfer.FLAG_DISABLE;
                            }
                        }



                    }

                    log.debug("src body:" + srcText);
                    String decryptText = secureTransfer.decrypt(srcText, aesKey);
                    log.debug("decrypt body:" + decryptText);
                    // 将解密的数据重新包装
                    requestProxyWrapper = new HttpServletRequestProxyWrapper(request, decryptText.getBytes(request.getCharacterEncoding()));

                }
                log.debug("mark as decrypted.");
                // 标记已被解密
                requestProxyWrapper.setAttachHeader(SecureTransfer.FILTER_DECRYPT_HEADER, SecureTransfer.FLAG_ENABLE);

                // 标记签名验证
                if(signPassHeader!=null) {
                    requestProxyWrapper.setAttachHeader(SecureTransfer.FILTER_SIGN_PASS_HEADER, signPassHeader);
                }

                // 标记一次性签名验证
                if(signNoncePassHeader!=null) {
                    requestProxyWrapper.setAttachHeader(SecureTransfer.FILTER_SIGN_NONCE_PASS_HEADER, signNoncePassHeader);
                }
                nextRequest = requestProxyWrapper;
            }
        }


        log.debug("chain next...");

        log.debug("next request:" + nextRequest.getClass().getName());
        log.debug("next response:" + nextResponse.getClass().getName());

        // 开始进入过滤器
        chain.doFilter(nextRequest, nextResponse);

        if (ann == null || !ann.out()) {
            log.debug("not require secure response.");
            return;
        }

        HttpServletResponseProxyWrapper responseProxyWrapper = (HttpServletResponseProxyWrapper) nextResponse;

        // 加密响应体
        byte[] edata = responseProxyWrapper.getBodyBytes();

        // 只有响应头包含加密头时，才加密请求
        if (edata.length > 0 ) {
            log.debug("find response secure.");

            if (secureConfig.getResponseCharset() != null && !"".equals(secureConfig.getResponseCharset())) {
                log.debug("reset response charset as:" + secureConfig.getResponseCharset());
                // 重置字符编码
                nextResponse.setCharacterEncoding(secureConfig.getResponseCharset());
            }

            // 每次生成随机aes加密秘钥
            String aesKey = secureTransfer.aesKeyGen();

            // 设置aes秘钥到响应头
            secureTransfer.setResponseSecureHeader(response, aesKey);

            String enData = null;

            log.debug("encrypt response body...");

            // 特殊处理返回值为string类型的情况
            String strHeader = responseProxyWrapper.getHeader(SecureTransfer.STRING_RETURN_HEADER);
            if (SecureTransfer.FLAG_ENABLE.equals(strHeader)) {
                String retStr = new String(edata, responseProxyWrapper.getCharacterEncoding());
                enData = secureTransfer.encrypt(retStr, aesKey);
            } else {
                // 使用AES秘钥加密响应体
                enData = secureTransfer.encryptJsonBytes(edata, aesKey);
            }

            // 写回数据体
            edata = enData.getBytes(responseProxyWrapper.getCharacterEncoding());

            log.debug("rewrite content-type/content-length");

            // 重置响应类型
            responseProxyWrapper.setContentType("text/plain");

            // 重置响应长度
            response.setContentLengthLong(enData.length());
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
