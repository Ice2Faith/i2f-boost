package i2f.spring.secret.web.exception;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/10/21 9:22
 * @desc
 */
public interface SecretFilterExceptionHandler {
    void handle(HttpServletRequest request, HttpServletResponse response, Throwable e) throws ServletException, IOException;
}
