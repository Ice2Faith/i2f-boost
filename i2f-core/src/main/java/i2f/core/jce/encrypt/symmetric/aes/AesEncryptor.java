package i2f.core.jce.encrypt.symmetric.aes;

import i2f.core.jce.encrypt.symmetric.basic.BasicEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用SecretKeySpec作为秘钥生成器，固定长度16位
 */
public class AesEncryptor extends BasicEncryptor {

    public AesEncryptor(byte[] secretBytes) {
        super(AesType.ECB_PKCS5PADDING,secretBytes);
    }

    public AesEncryptor(AesType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public AesEncryptor(AesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }
}
