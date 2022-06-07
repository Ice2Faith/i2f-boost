package i2f.springboot.property.impl;

import i2f.core.digest.AESUtil;
import i2f.springboot.property.core.ITextEncryptor;
import i2f.springboot.property.core.PrefixPropertyDecryptor;

/**
 * @author ltb
 * @date 2022/6/7 9:59
 * @desc
 */
public class AesPropertyDecryptor extends PrefixPropertyDecryptor implements ITextEncryptor {
    public static final String AES_PREFIX="aes.";
    private String key;
    public AesPropertyDecryptor(String key) {
        super(AES_PREFIX);
        this.key=AESUtil.genKey(key);
    }

    @Override
    public String decryptText(String text) {
        try{
            byte[] data = AESUtil.decrypt(text, key);
            return new String(data,"UTF-8");
        }catch(Exception e){
            return text;
        }
    }

    @Override
    public String encrypt(String text) {
        try{
            return AES_PREFIX+AESUtil.encrypt(text.getBytes("UTF-8"),key);
        }catch(Exception e){
            return AES_PREFIX+text;
        }
    }
}
