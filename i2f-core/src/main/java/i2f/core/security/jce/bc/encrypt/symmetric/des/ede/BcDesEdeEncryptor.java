package i2f.core.security.jce.bc.encrypt.symmetric.des.ede;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.encrypt.std.symmetric.basic.BasicSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES-EDE加解密器
 */
public class BcDesEdeEncryptor extends BasicSymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcDesEdeEncryptor(byte[] secretBytes) {
        super(BcDesEdeType.ECB_PKCS5PADDING, secretBytes);
    }

    public BcDesEdeEncryptor() {
        super(BcDesEdeType.ECB_PKCS5PADDING);
    }

    public BcDesEdeEncryptor(BcDesEdeType type) {
        super(type);
    }

    public BcDesEdeEncryptor(BcDesEdeType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcDesEdeEncryptor(BcDesEdeType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

}
