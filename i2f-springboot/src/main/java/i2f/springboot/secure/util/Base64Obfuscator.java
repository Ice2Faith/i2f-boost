package i2f.springboot.secure.util;

import java.util.Random;

/**
 * @author ltb
 * @date 2022/5/6 15:27
 * @desc base64 混淆器实现
 * 每隔原始的base64串2位，随机插入一个hex的char
 */
public class Base64Obfuscator {
    public static final String OBF_PREFIX ="$.";
    public static String encode(String bs4,boolean prefix){
        Random random=new Random();
        StringBuilder builder = new StringBuilder();
        if(prefix){
            builder.append(OBF_PREFIX);
        }
        for(int i=0;i<bs4.length();i++){
            if(i%2==0 && bs4.charAt(i)!='='){
                int ird=random.nextInt(16);
                if(ird<10){
                    builder.append((char)('0'+ird));
                }else{
                    builder.append((char)('A'+(ird-10)));
                }
            }
            builder.append(bs4.charAt(i));
        }
        return builder.toString();
    }
    public static String decode(String sob){
        String str=sob;
        if(sob.startsWith(OBF_PREFIX)){
            str=sob.substring(OBF_PREFIX.length());
        }
        StringBuilder builder = new StringBuilder();
        int i=0;
        while(i<str.length()){
            if(i%3==0 && str.charAt(i)!='='){
                i++;
                continue;
            }
            builder.append(str.charAt(i));
            i++;
        }
        return builder.toString();
    }

}
