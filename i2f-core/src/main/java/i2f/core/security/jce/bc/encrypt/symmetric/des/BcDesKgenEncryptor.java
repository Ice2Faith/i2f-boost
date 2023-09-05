package i2f.core.security.jce.bc.encrypt.symmetric.des;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicKgenSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES加解密器
 */
public class BcDesKgenEncryptor extends BasicKgenSymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcDesKgenEncryptor(byte[] secretBytes) {
        super(BcDesType.ECB_PKCS5PADDING, secretBytes);
    }

    public BcDesKgenEncryptor() {
        super(BcDesType.ECB_PKCS5PADDING);
    }

    public BcDesKgenEncryptor(BcDesType type) {
        super(type);
    }

    public BcDesKgenEncryptor(BcDesType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcDesKgenEncryptor(BcDesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }


}
