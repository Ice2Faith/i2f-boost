package i2f.core.jce.encrypt.aes;

import i2f.core.jce.encrypt.BasicKgenEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用KeyGenerator作为秘钥生成器，密码可以随机长度，内部其实是作为种子使用
 */
public class AesKgenEncryptor extends BasicKgenEncryptor {

    public AesKgenEncryptor(byte[] secretBytes) {
        super(AesType.CBC_NO_PADDING,secretBytes);
    }

    public AesKgenEncryptor(AesType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public AesKgenEncryptor(AesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }

}
