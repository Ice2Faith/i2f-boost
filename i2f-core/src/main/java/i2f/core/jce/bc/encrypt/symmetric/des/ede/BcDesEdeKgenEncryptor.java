package i2f.core.jce.bc.encrypt.symmetric.des.ede;

import i2f.core.jce.bc.BouncyCastleHolder;
import i2f.core.jce.encrypt.std.symmetric.basic.BasicKgenSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc DES-EDE加解密器
 */
public class BcDesEdeKgenEncryptor extends BasicKgenSymmetricEncryptor {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcDesEdeKgenEncryptor(byte[] secretBytes) {
        super(BcDesEdeType.ECB_PKCS5PADDING, secretBytes);
    }

    public BcDesEdeKgenEncryptor(BcDesEdeType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcDesEdeKgenEncryptor(BcDesEdeType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

}
