package i2f.spring.secret.web.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/10/21 9:28
 * @desc
 */
public class ForwardSecretFilterExceptionHandler implements SecretFilterExceptionHandler {
    public static final String FORWARD_EXCEPTION_ATTRIBUTE = "exceptionForward";
    public String forwardTarget = "/exception";

    public ForwardSecretFilterExceptionHandler() {
    }

    public ForwardSecretFilterExceptionHandler(String forwardTarget) {
        this.forwardTarget = forwardTarget;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Throwable e) throws ServletException, IOException {
        request.setAttribute(FORWARD_EXCEPTION_ATTRIBUTE, e);
        request.getRequestDispatcher(forwardTarget).forward(request, response);
    }
}
