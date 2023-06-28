package i2f.core.j2ee.token;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.safe.token.ISignCalculator;
import i2f.core.safe.token.ITokenGenerator;
import i2f.core.safe.token.TokenManager;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/5/25 9:02
 * @desc 隔离用户的IP和浏览器的token生成校验器
 * 当用户的IP或者浏览器二者任何一个发生改变，将校验不通过
 * 作用，限制token只能在同一个IP的同一个浏览器使用
 */
public class WebHostUserAgentTokenManager extends TokenManager implements IWebTokenManager {

    public WebHostUserAgentTokenManager() {
    }

    public WebHostUserAgentTokenManager(ISignCalculator signCalculator, ITokenGenerator tokenGenerator) {
        super(signCalculator, tokenGenerator);
    }

    @Override
    public String makeToken(HttpServletRequest request, Object... args) {
        String ip= ServletContextUtil.getIp(request);
        String ipSign=signCalculator.sign(ip);
        String body = tokenGenerator.token(args);

        String userAgent= ServletContextUtil.getUserAgent(request);
        String userAgentSign=signCalculator.sign(userAgent);

        String ipUserAgentBody=ipSign+SEP_CHAR+userAgentSign+SEP_CHAR+body;
        return signToken(ipUserAgentBody);
    }

    @Override
    public boolean verifyToken(HttpServletRequest request, String token) {
        if(!verifyToken(token)){
            return false;
        }

        String ip= ServletContextUtil.getIp(request);
        String cipSign=signCalculator.sign(ip);

        String userAgent= ServletContextUtil.getUserAgent(request);
        String cuserAgentSign=signCalculator.sign(userAgent);

        String[] arr=token.split(SEP_CHAR,4);
        if(arr.length!=4){
            return false;
        }

        String ipSign=arr[1];
        String userAgentSign=arr[2];
        return cipSign.equalsIgnoreCase(ipSign) && cuserAgentSign.equalsIgnoreCase(userAgentSign);
    }

    @Override
    public String tokenBody(String token) {
        return token.split(SEP_CHAR,4)[3];
    }

}
