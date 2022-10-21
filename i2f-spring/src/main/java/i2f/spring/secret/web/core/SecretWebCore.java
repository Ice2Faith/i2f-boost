package i2f.spring.secret.web.core;

import i2f.core.j2ee.web.HttpServletRequestProxyWrapper;
import i2f.core.j2ee.web.HttpServletResponseProxyWrapper;
import i2f.core.secret.api.key.IKeyPair;
import i2f.core.secret.core.SecretProvider;
import i2f.core.secret.data.Base64SecretMsg;
import i2f.core.secret.data.SecretKeyPair;
import i2f.core.secret.data.SecretMsg;
import i2f.core.secret.exception.SecretException;
import i2f.core.secret.util.SecretUtil;
import i2f.spring.mapping.MappingUtil;
import i2f.spring.secret.web.annotations.SecretParams;
import i2f.spring.secret.web.exception.SecretFilterExceptionHandler;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author ltb
 * @date 2022/10/20 16:47
 * @desc
 */
public class SecretWebCore {
    public static final String HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    public static final String HTTP_HEADER_ACCESS_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static final String HEADER_RANDOM_KEY = "rdk";
    public static final String HEADER_SIGN_KEY = "sign";
    public static final String HEADER_NONCE_KEY = "nonce";
    public static final String HEADER_PUB_KEY = "spk";

    public ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<>();
    public ThreadLocal<HttpServletResponse> responseHolder = new ThreadLocal<>();
    public ThreadLocal<Base64SecretMsg> secretHeaderHolder = new ThreadLocal<>();

    public SecretFilterExceptionHandler exceptionHandler;

    public List<String> whiteList = new ArrayList<>();

    public SecretProvider secretProvider;

    public static AntPathMatcher antPathMatcher = new AntPathMatcher();

    public MappingUtil mappingUtil;

    public SecretWebCore(MappingUtil mappingUtil) {
        this.mappingUtil = mappingUtil;
    }

    public SecretWebCore(MappingUtil mappingUtil, List<String> whiteList) {
        this.whiteList = whiteList;
        this.mappingUtil = mappingUtil;
    }

    public SecretWebCore(MappingUtil mappingUtil, List<String> whiteList, SecretProvider secretProvider) {
        this.whiteList = whiteList;
        this.secretProvider = secretProvider;
        this.mappingUtil = mappingUtil;
    }

    public SecretProvider secret() {
        return secretProvider;
    }

    public SecretParams getMappingHandlerSecretParams(HttpServletRequest request) {
        Method method = getMappingHandlerMethod(request);
        if (method == null) {
            return null;
        }
        SecretParams ann = getAnnotation(method, SecretParams.class);
        if (ann == null) {
            ann = getAnnotation(method.getDeclaringClass(), SecretParams.class);
        }
        return ann;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ret = elem.getDeclaredAnnotation(clazz);
        if (ret == null) {
            ret = elem.getAnnotation(clazz);
        }
        return ret;
    }

    public Method getMappingHandlerMethod(HttpServletRequest request) {
        return mappingUtil.getRequestMappingMethod(request);
    }

    public boolean isWhiteListMapping(HttpServletRequest request) {
        LinkedHashSet<String> list = getMappingWhiteList();
        String requestUrl = request.getRequestURI();
        for (String item : list) {
            if (antPathMatcher.match(item, requestUrl)) {
                return true;
            }
        }
        return false;
    }

    public LinkedHashSet<String> getMappingWhiteList() {
        LinkedHashSet<String> ret = new LinkedHashSet<>();
        ret.addAll(whiteList);
        return ret;
    }

    public HttpServletRequestProxyWrapper wrapRequest(HttpServletRequest request) throws IOException {
        return new HttpServletRequestProxyWrapper(request);
    }

    public HttpServletResponseProxyWrapper wrapResponse(HttpServletResponse response) throws UnsupportedEncodingException {
        return new HttpServletResponseProxyWrapper(response);
    }

    public HttpServletRequestProxyWrapper wrapRequest(HttpServletRequest request, SecretMsg msg) throws IOException {
        return new HttpServletRequestProxyWrapper(request, msg.msg);
    }

    public Base64SecretMsg parseMsgHeaders(HttpServletRequest request) {
        Base64SecretMsg ret = new Base64SecretMsg();
        ret.signature = request.getHeader(HEADER_SIGN_KEY);
        ret.randomKey = request.getHeader(HEADER_RANDOM_KEY);
        ret.nonce = request.getHeader(HEADER_NONCE_KEY);
        ret.publicKey = request.getHeader(HEADER_PUB_KEY);
        return ret;
    }

    public SecretMsg parseMsg(HttpServletRequest request) throws IOException {
        Base64SecretMsg ret = new Base64SecretMsg();
        ret.signature = request.getHeader(HEADER_SIGN_KEY);
        ret.randomKey = request.getHeader(HEADER_RANDOM_KEY);
        ret.nonce = request.getHeader(HEADER_NONCE_KEY);
        ret.publicKey = request.getHeader(HEADER_PUB_KEY);
        ServletInputStream is = request.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder builder = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        is.close();
        ret.msg = builder.toString();
        try {
            SecretMsg msg = ret.convert();
            byte[] data = secret().recv(msg);
            msg.msg = data;
            return msg;
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public SecretMsg secretMsg(HttpServletResponseProxyWrapper response, Base64SecretMsg secretHeader) throws IOException {
        try {
            SecretMsg header = secretHeader.convert();
            IKeyPair key = new SecretKeyPair(header.publicKey);
            return secret().send(response.getBodyBytes(), key);
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    public void writeMsg(HttpServletResponse response, SecretMsg msg) throws IOException {
        Base64SecretMsg data = msg.convert();
        response.setContentType("text/plain");

        response.setHeader(HEADER_SIGN_KEY, data.signature);
        response.setHeader(HEADER_RANDOM_KEY, data.randomKey);
        response.setHeader(HEADER_NONCE_KEY, data.nonce);
        response.setHeader(HEADER_PUB_KEY, data.publicKey);

        String accessHeaders = HEADER_SIGN_KEY + ", " + HEADER_RANDOM_KEY + ", " + HEADER_NONCE_KEY + ", " + HEADER_PUB_KEY;

        response.setHeader(HTTP_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS, accessHeaders);
        response.setHeader(HTTP_HEADER_ACCESS_ALLOW_HEADERS, accessHeaders);

        byte[] body = SecretUtil.str2utf8(data.msg);

        response.setContentLengthLong(body.length);

        OutputStream os = response.getOutputStream();
        os.write(body);
        os.flush();
    }
}
