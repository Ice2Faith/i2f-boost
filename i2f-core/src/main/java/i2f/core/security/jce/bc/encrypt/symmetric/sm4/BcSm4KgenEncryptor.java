package i2f.core.security.jce.bc.encrypt.symmetric.sm4;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicKgenSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用KeyGenerator作为秘钥生成器，密码可以随机长度，内部其实是作为种子使用
 */
public class BcSm4KgenEncryptor extends BasicKgenSymmetricEncryptor {

    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcSm4KgenEncryptor(byte[] secretBytes) {
        super(BcSm4Type.ECB_PKCS5PADDING, secretBytes);
    }

    public BcSm4KgenEncryptor() {
        super(BcSm4Type.ECB_PKCS5PADDING);
    }

    public BcSm4KgenEncryptor(BcSm4Type type) {
        super(type);
    }

    public BcSm4KgenEncryptor(BcSm4Type type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcSm4KgenEncryptor(BcSm4Type type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

}
