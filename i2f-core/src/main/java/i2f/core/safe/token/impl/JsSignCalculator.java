package i2f.core.safe.token.impl;

import i2f.core.safe.token.ISignCalculator;

/**
 * @author ltb
 * @date 2022/5/25 8:49
 * @desc 需要一个兼容JS能够也进行相同签名的简单签名
 * 方便JS端也能够进行签名和验签
 * 这里提供了对应的JS函数，在js-sign.js中定义
 */
public class JsSignCalculator implements ISignCalculator {
    @Override
    public String sign(String text) {
        text=text.length()+":"+text;
        int sval0=7;
        int sval1=19;
        int sval2=27;
        int sval3=31;
        int sval4=17;
        int sval5=23;
        int cnt=6;

        int len=text.length();
        for(int i=0;i<len;i++){
            char ch=text.charAt(i);
            int iv=((int)ch)+(int)(i*100/len);
            if(iv%cnt==0){
                sval0=((sval0+iv)*7)%700;
            }else if(iv%cnt==1){
                sval1=((sval1+iv)*19)%1900;
            }else if(iv%cnt==2){
                sval2=((sval2+iv)*27)%2700;
            }else if(iv%cnt==3){
                sval3=((sval3+iv)*31)%3100;
            }else if(iv%cnt==4){
                sval4=((sval4+iv)*17)%1700;
            }else if(iv%cnt==5){
                sval5=((sval5+iv)*23)%2300;
            }
        }
        return Integer.toHexString(sval0)
                +""+Integer.toHexString(sval1)
                +""+Integer.toHexString(sval2)
                +""+Integer.toHexString(sval3)
                +""+Integer.toHexString(sval4)
                +""+Integer.toHexString(sval5);
    }
}
