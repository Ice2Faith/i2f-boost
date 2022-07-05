package i2f.springboot.secure.advice;


import i2f.springboot.secure.annotation.SecureParams;
import i2f.springboot.secure.core.SecureTransfer;
import i2f.springboot.secure.exception.SecureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

//注意，接受参数需要加上 @RequestBody注解，否则不会被拦截，另外需要以POST方式application/json方式提交
//原因间此类中的调用：RequestResponseBodyMethodProcessor
@ConditionalOnExpression("${i2f.springboot.config.secure.request-advice.enable:true}")
@Slf4j
@ControllerAdvice
public class RequestBodyDecryptAdvice implements RequestBodyAdvice, InitializingBean {

    @Autowired
    private SecureTransfer secureTransfer;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("RequestBodyDecryptAdvice config done.");
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        if(methodParameter.getMethod().isAnnotationPresent(SecureParams.class)){
            SecureParams secureParams=methodParameter.getMethodAnnotation(SecureParams.class);
            if(secureParams.in()){
                return new RequestHttpInputMessage(httpInputMessage);
            }
        }
        return httpInputMessage;
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    public class RequestHttpInputMessage implements HttpInputMessage{
        public InputStream body;
        public HttpHeaders headers;
        public RequestHttpInputMessage(HttpInputMessage inputMessage) throws IOException {
            this.headers=inputMessage.getHeaders();
            // 判断是否发生异常
            String exceptionHeader=this.headers.getFirst(SecureTransfer.FILTER_EXCEPTION_ATTR_KEY);
            if(SecureTransfer.FLAG_ENABLE.equals(exceptionHeader)){
                throw new SecureException("秘钥过期或已失效，请重试！");
            }

            // 判断是否包含加密头，包含则需要解密
            String aesKeyTransfer=this.headers.getFirst(SecureTransfer.SECURE_DATA_HEADER);
            String rsaSign=this.headers.getFirst(SecureTransfer.SECURE_RSA_PUB_KEY_OR_SIGN_HEADER);

            boolean isEncrypt=aesKeyTransfer!=null && !"".equals(aesKeyTransfer);

            // 如果有使用过滤器，则需要判断过滤器是否已经解密，已经解密则直接跳过，不重复解密
            String decryptFlag=this.headers.getFirst(SecureTransfer.FILTER_DECRYPT_HEADER);
            if(SecureTransfer.FLAG_ENABLE.equals(decryptFlag)){
                isEncrypt=false;
            }

            // 读取请求体
            String bodyStr = FileCopyUtils.copyToString(new InputStreamReader(inputMessage.getBody()));
            System.out.println("bodyStr:" + bodyStr);

            // 需要解密，并且请求体不为空时解密
            if(isEncrypt && bodyStr!=null && !"".equals(bodyStr)) {
                String aesKey=secureTransfer.getRequestSecureHeader(aesKeyTransfer,rsaSign);
                System.out.println("bodyStr:" + bodyStr);
                String data=secureTransfer.decrypt(bodyStr,aesKey);
                System.out.println("data:" + data);
                byte[] deData = data.getBytes();
                this.body = new ByteArrayInputStream(deData);
            }else{
                byte[] deData = bodyStr.getBytes();
                this.body = new ByteArrayInputStream(deData);
            }
        }
        @Override
        public InputStream getBody() throws IOException {
            return body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return headers;
        }
    }
}
