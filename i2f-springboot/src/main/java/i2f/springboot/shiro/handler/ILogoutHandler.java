package i2f.springboot.shiro.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/23 17:29
 * @desc
 */
public interface ILogoutHandler {
    void handle(boolean fullMode, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
