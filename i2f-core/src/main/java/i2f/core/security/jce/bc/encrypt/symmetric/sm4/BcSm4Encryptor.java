package i2f.core.security.jce.bc.encrypt.symmetric.sm4;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用SecretKeySpec作为秘钥生成器，固定长度16位
 */
public class BcSm4Encryptor extends BasicSymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcSm4Encryptor(byte[] secretBytes) {
        super(BcSm4Type.ECB_PKCS5PADDING, secretBytes);
    }

    public BcSm4Encryptor() {
        super(BcSm4Type.ECB_PKCS5PADDING);
    }

    public BcSm4Encryptor(BcSm4Type type) {
        super(type);
    }

    public BcSm4Encryptor(BcSm4Type type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcSm4Encryptor(BcSm4Type type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }
}
