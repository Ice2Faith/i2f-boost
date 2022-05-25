package i2f.core.safe.token;

import i2f.core.safe.token.impl.JsSignCalculator;
import i2f.core.safe.token.impl.UuidTokenGenerator;

/**
 * @author ltb
 * @date 2022/5/25 8:44
 * @desc token的生成与签名校验器
 */
public class TokenManager {
    public static final String SEP_CHAR="-";

    protected ISignCalculator signCalculator=new JsSignCalculator();
    protected ITokenGenerator tokenGenerator=new UuidTokenGenerator();
    public TokenManager(){

    }
    public TokenManager(ISignCalculator signCalculator,ITokenGenerator tokenGenerator){
        this.signCalculator=signCalculator;
        this.tokenGenerator=tokenGenerator;
    }

    public String signToken(String body){
        String sign=signCalculator.sign(body);
        String token=sign+SEP_CHAR+body;
        return token;
    }

    public String makeToken(Object ... args){
        String body = tokenGenerator.token(args);
        return signToken(body);
    }

    public boolean verifyToken(String token){
        String[] arr=token.split(SEP_CHAR,2);
        if(arr.length!=2){
            return false;
        }

        String sign=arr[0];
        String body=arr[1];

        String csign=signCalculator.sign(body);
        return csign.equalsIgnoreCase(sign);
    }

    public String tokenBody(String token){
        return token.split(SEP_CHAR,2)[1];
    }

}
