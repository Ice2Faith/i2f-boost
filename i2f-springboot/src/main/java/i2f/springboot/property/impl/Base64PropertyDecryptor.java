package i2f.springboot.property.impl;

import i2f.springboot.property.core.ITextEncryptor;
import i2f.springboot.property.core.PrefixPropertyDecryptor;

import java.util.Base64;

/**
 * @author ltb
 * @date 2022/6/7 9:59
 * @desc
 */
public class Base64PropertyDecryptor extends PrefixPropertyDecryptor implements ITextEncryptor {
    public static final String BASE64_PREFIX="bs64.";
    public Base64PropertyDecryptor() {
        super(BASE64_PREFIX);
    }

    @Override
    public String decryptText(String text) {
        try{
            byte[] data=Base64.getDecoder().decode(text);
            return new String(data,"UTF-8");
        }catch(Exception e){
            return text;
        }
    }

    @Override
    public String encrypt(String text) {
        try{
            return BASE64_PREFIX+Base64.getEncoder().encodeToString(text.getBytes("UTF-8"));
        }catch(Exception e){
            return BASE64_PREFIX+text;
        }
    }
}
