package i2f.core.j2ee.web.safe.token;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.safe.token.ISignCalculator;
import i2f.core.safe.token.ITokenGenerator;
import i2f.core.safe.token.TokenManager;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/5/25 9:02
 * @desc 隔离用户的浏览器的token生成校验器
 * 当用户切换了不同的浏览器时，将验证不通过，除非浏览器具有相同的UserAgent
 * 作用，限制token只能在同一个浏览器使用
 */
public class WebUserAgentTokenManager extends TokenManager implements IWebTokenManager {
    public WebUserAgentTokenManager() {
    }

    public WebUserAgentTokenManager(ISignCalculator signCalculator, ITokenGenerator tokenGenerator) {
        super(signCalculator, tokenGenerator);
    }

    @Override
    public String makeToken(HttpServletRequest request, Object... args) {
        String userAgent= ServletContextUtil.getUserAgent(request);
        String userAgentSign=signCalculator.sign(userAgent);
        String body = tokenGenerator.token(args);
        String userAgentBody=userAgentSign+SEP_CHAR+body;
        return signToken(userAgentBody);
    }

    @Override
    public boolean verifyToken(HttpServletRequest request, String token) {
        if(!verifyToken(token)){
            return false;
        }
        String userAgent= ServletContextUtil.getUserAgent(request);
        String cuserAgentSign=signCalculator.sign(userAgent);

        String[] arr=token.split(SEP_CHAR,3);
        if(arr.length!=3){
            return false;
        }

        String userAgentSign=arr[1];
        return cuserAgentSign.equalsIgnoreCase(userAgentSign);
    }

    @Override
    public String tokenBody(String token) {
        return token.split(SEP_CHAR,3)[2];
    }
}
