package i2f.core.jce.encrypt;

import i2f.core.jce.encrypt.symmetric.aes.AesEncryptor;
import i2f.core.jce.encrypt.symmetric.aes.AesKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.aes.AesType;
import i2f.core.jce.encrypt.symmetric.des.DesEncryptor;
import i2f.core.jce.encrypt.symmetric.des.DesKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.des.DesType;
import i2f.core.jce.encrypt.symmetric.des.ede.DesEdeEncryptor;
import i2f.core.jce.encrypt.symmetric.des.ede.DesEdeKgenEncryptor;
import i2f.core.jce.encrypt.symmetric.des.ede.DesEdeType;

/**
 * @author ltb
 * @date 2022/6/9 18:08
 * @desc
 */
public class EncryptUtil {


    public static AesEncryptor aes(byte[] secretKey){
        return new AesEncryptor(secretKey);
    }
    public static AesEncryptor aes(AesType type,byte[] secretKey){
        return new AesEncryptor(type,secretKey);
    }
    public static AesEncryptor aes(AesType type,byte[] secretKey,byte[] vectorKey){
        return new AesEncryptor(type,secretKey,vectorKey);
    }

    public static AesKgenEncryptor aesKgen(byte[] secretKey){
        return new AesKgenEncryptor(secretKey);
    }
    public static AesKgenEncryptor aesKgen(AesType type, byte[] secretKey){
        return new AesKgenEncryptor(type,secretKey);
    }
    public static AesKgenEncryptor aesKgen(AesType type, byte[] secretKey,byte[] vectorKey){
        return new AesKgenEncryptor(type,secretKey,vectorKey);
    }




    public static DesEncryptor des(byte[] secretKey){
        return new DesEncryptor(secretKey);
    }
    public static DesEncryptor des(DesType type, byte[] secretKey){
        return new DesEncryptor(type,secretKey);
    }
    public static DesEncryptor des(DesType type,byte[] secretKey,byte[] vectorKey){
        return new DesEncryptor(type,secretKey,vectorKey);
    }

    public static DesKgenEncryptor desKgen(byte[] secretKey){
        return new DesKgenEncryptor(secretKey);
    }
    public static DesKgenEncryptor desKgen(DesType type, byte[] secretKey){
        return new DesKgenEncryptor(type,secretKey);
    }
    public static DesKgenEncryptor desKgen(DesType type, byte[] secretKey,byte[] vectorKey){
        return new DesKgenEncryptor(type,secretKey,vectorKey);
    }


    public static DesEdeEncryptor desEde(byte[] secretKey){
        return new DesEdeEncryptor(secretKey);
    }
    public static DesEdeEncryptor desEde(DesEdeType type, byte[] secretKey){
        return new DesEdeEncryptor(type,secretKey);
    }
    public static DesEdeEncryptor desEde(DesEdeType type,byte[] secretKey,byte[] vectorKey){
        return new DesEdeEncryptor(type,secretKey,vectorKey);
    }

    public static DesEdeKgenEncryptor desEdeKgen(byte[] secretKey){
        return new DesEdeKgenEncryptor(secretKey);
    }
    public static DesEdeKgenEncryptor desEdeKgen(DesEdeType type, byte[] secretKey){
        return new DesEdeKgenEncryptor(type,secretKey);
    }
    public static DesEdeKgenEncryptor desEdeKgen(DesEdeType type, byte[] secretKey,byte[] vectorKey){
        return new DesEdeKgenEncryptor(type,secretKey,vectorKey);
    }
}
