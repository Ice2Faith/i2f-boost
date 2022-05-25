package i2f.core.j2ee.web.safe.token;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/5/25 9:02
 * @desc
 */
public interface IWebTokenManager {

    String makeToken(HttpServletRequest request,Object ... args);

    boolean verifyToken(HttpServletRequest request,String token);

    String tokenBody(String token);

}
