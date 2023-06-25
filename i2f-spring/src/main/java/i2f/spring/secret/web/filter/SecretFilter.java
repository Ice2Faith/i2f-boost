package i2f.spring.secret.web.filter;

import i2f.core.j2ee.web.HttpServletResponseProxyWrapper;
import i2f.core.security.secret.data.Base64SecretMsg;
import i2f.core.security.secret.data.SecretMsg;
import i2f.core.security.secret.exception.SecretException;
import i2f.spring.secret.web.annotations.SecretParams;
import i2f.spring.secret.web.core.SecretWebCore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/10/20 16:46
 * @desc
 */
@Slf4j
@Order(1)
@WebFilter(urlPatterns = "/**")
public class SecretFilter extends OncePerRequestFilter {


    public SecretWebCore secretWebCore;

    public SecretFilter(SecretWebCore secretWebCore) {
        this.secretWebCore = secretWebCore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        Base64SecretMsg secretHeader = secretWebCore.parseMsgHeaders(request);
        try {
            secretWebCore.requestHolder.set(request);
            secretWebCore.responseHolder.set(response);
            secretWebCore.secretHeaderHolder.set(secretHeader);
            filterProxy(request, response, chain, secretHeader);
        } catch (Throwable e) {
            onFilterException(request, response, e);
        } finally {
            secretWebCore.requestHolder.remove();
            secretWebCore.responseHolder.remove();
            secretWebCore.secretHeaderHolder.remove();

        }

    }

    protected void onFilterException(HttpServletRequest request, HttpServletResponse response, Throwable e) throws ServletException, IOException {
        if (secretWebCore.exceptionHandler != null) {
            secretWebCore.exceptionHandler.handle(request, response, e);
        } else {
            throw new SecretException(e);
        }
    }

    protected void filterProxy(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Base64SecretMsg secretHeader) throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.addHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
        response.addHeader("Access-Control-Max-Age", "120");

        // 白名单内，直接进入
        if (secretWebCore.isWhiteListMapping(request) || request.getMethod().toLowerCase().contains("options")) {
            chain.doFilter(request, response);
            return;
        }

        // 获取可能的处理器注解
        SecretParams ann = secretWebCore.getMappingHandlerSecretParams(request);
        boolean secretInFlag = ann != null && ann.in();
        boolean secretOutFlat = ann != null && ann.out();

        if (!secretInFlag && !secretOutFlat) {
            chain.doFilter(request, response);
            return;
        }

        // 包装的请求和响应
        HttpServletRequest nextRequest = request;
        HttpServletResponse nextResponse = response;

        // 如果安全输入，需要提前包装响应
        if (secretOutFlat) {
            nextResponse = secretWebCore.wrapResponse(response);
        }

        // 如果安全输入，则需要解密重新包装
        if (secretInFlag) {
            SecretMsg msg = secretWebCore.parseMsg(request);
            nextRequest = secretWebCore.wrapRequest(request, msg);
        }

        // 进入处理
        chain.doFilter(nextRequest, nextResponse);

        // 不需要安全输出，直接结束
        if (!secretOutFlat) {
            return;
        }

        // 需要安全输出，处理转换值，重新写会响应
        SecretMsg ret = secretWebCore.secretMsg((HttpServletResponseProxyWrapper) nextResponse, secretHeader);
        secretWebCore.writeMsg(response, ret);
    }


}
