package i2f.spring.secret.web.filter;

import i2f.core.j2ee.web.HttpServletResponseProxyWrapper;
import i2f.core.secret.data.SecretMsg;
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
        // 白名单内，直接进入
        if (secretWebCore.isWhiteListMapping(request)) {
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
        SecretMsg ret = secretWebCore.secretMsg((HttpServletResponseProxyWrapper) nextResponse);
        secretWebCore.writeMsg(response, ret);

    }


}
