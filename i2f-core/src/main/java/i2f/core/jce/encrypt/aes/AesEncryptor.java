package i2f.core.jce.encrypt.aes;

import i2f.core.jce.encrypt.BasicEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用SecretKeySpec作为秘钥生成器，固定长度16位
 */
public class AesEncryptor extends BasicEncryptor {

    public AesEncryptor(byte[] secretBytes) {
        super(AesType.CBC_NO_PADDING,secretBytes);
    }

    public AesEncryptor(AesType type, byte[] secretBytes) {
        super(type,secretBytes);
    }

    public AesEncryptor(AesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type,secretBytes,vectorBytes);
    }
}
