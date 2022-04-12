package i2f.springboot.security;

import i2f.core.api.ApiResp;
import i2f.core.j2ee.web.ServletContextUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ltb
 * @date 2022/4/7 17:13
 * @desc
 */
public class SecurityForwardUtil {
    public static final String X_API_RESP_KEY="x-api-resp";
    public static final String X_FORWARD_PATH="/security/response";

    public static void forward(HttpServletRequest request, HttpServletResponse response, ApiResp resp) throws ServletException, IOException {
        request.setAttribute(X_API_RESP_KEY, resp);
        ServletContextUtil.forward(request,response,X_FORWARD_PATH);
    }

    public static ApiResp getResp(HttpServletRequest request){
        return (ApiResp)request.getAttribute(X_API_RESP_KEY);
    }
}
