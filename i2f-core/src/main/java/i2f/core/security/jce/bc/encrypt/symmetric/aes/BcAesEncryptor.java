package i2f.core.security.jce.bc.encrypt.symmetric.aes;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用SecretKeySpec作为秘钥生成器，固定长度16位
 */
public class BcAesEncryptor extends BasicSymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcAesEncryptor(byte[] secretBytes) {
        super(BcAesType.ECB_PKCS5PADDING, secretBytes);
    }

    public BcAesEncryptor() {
        super(BcAesType.ECB_PKCS5PADDING);
    }

    public BcAesEncryptor(BcAesType type) {
        super(type);
    }

    public BcAesEncryptor(BcAesType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcAesEncryptor(BcAesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }
}
