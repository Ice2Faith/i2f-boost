package i2f.core.jce.bc.encrypt.symmetric.aes;

import i2f.core.jce.bc.BouncyCastleHolder;
import i2f.core.jce.encrypt.std.symmetric.basic.BasicKgenSymmetricEncryptor;

/**
 * @author ltb
 * @date 2022/6/8 9:17
 * @desc AES加解密器，使用KeyGenerator作为秘钥生成器，密码可以随机长度，内部其实是作为种子使用
 */
public class BcAesKgenEncryptor extends BasicKgenSymmetricEncryptor {

    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcAesKgenEncryptor(byte[] secretBytes) {
        super(BcAesType.ECB_PKCS5PADDING, secretBytes);
    }

    public BcAesKgenEncryptor(BcAesType type, byte[] secretBytes) {
        super(type, secretBytes);
    }

    public BcAesKgenEncryptor(BcAesType type, byte[] secretBytes, byte[] vectorBytes) {
        super(type, secretBytes, vectorBytes);
    }

}
