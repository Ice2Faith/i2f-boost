package i2f.core.digest;


/**
 * @author ltb
 * @date 2022/7/1 13:54
 * @desc
 */
public class StringSignature {
    public static String sign(String text) {
        int num1=0x51;
        int num2=0x73;
        int num3=0x117;
        int num4=0x19;
        int i=0;
        int len = text.length();
        while(i<len){
            char ch=text.charAt(i);
            int mod=i%4;
            if(mod==0){
                num1=(num1*7+ch+i/11)%0x07fff;
            }else if(mod==1){
                num2=(num2*13+ch+i/5)%0x07fff;
            }else if(mod==2){
                num3=(num3*3+ch+i/17)%0x07fff;
            }else if(mod==3){
                num4=(num4*31+ch+i/19)%0x07fff;
            }
            i++;
        }
        return String.format("%x%x%x%x%x",num1,num2,len,num3,num4);
    }

}
