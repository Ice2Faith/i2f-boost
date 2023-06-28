package i2f.core.j2ee.token;

import i2f.core.j2ee.web.ServletContextUtil;
import i2f.core.safe.token.ISignCalculator;
import i2f.core.safe.token.ITokenGenerator;
import i2f.core.safe.token.TokenManager;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ltb
 * @date 2022/5/25 9:02
 * @desc 隔离用户IP的token生成校验器
 * 当这个token所在的IP发生改变时，将验证不通过
 * 作用，限制token只能在同一个IP使用
 */
public class WebHostTokenManager extends TokenManager implements IWebTokenManager {

    public WebHostTokenManager() {
    }

    public WebHostTokenManager(ISignCalculator signCalculator, ITokenGenerator tokenGenerator) {
        super(signCalculator, tokenGenerator);
    }

    @Override
    public String makeToken(HttpServletRequest request, Object... args) {
        String ip= ServletContextUtil.getIp(request);
        String ipSign=signCalculator.sign(ip);
        String body = tokenGenerator.token(args);
        String ipBody=ipSign+SEP_CHAR+body;
        return signToken(ipBody);
    }

    @Override
    public boolean verifyToken(HttpServletRequest request, String token) {
        if(!verifyToken(token)){
            return false;
        }
        String ip= ServletContextUtil.getIp(request);
        String cipSign=signCalculator.sign(ip);

        String[] arr=token.split(SEP_CHAR,3);
        if(arr.length!=3){
            return false;
        }

        String ipSign=arr[1];
        return cipSign.equalsIgnoreCase(ipSign);
    }

    @Override
    public String tokenBody(String token) {
        return token.split(SEP_CHAR,3)[2];
    }

}
