package i2f.core.security.secret.impl.ram;

import i2f.core.security.secret.core.SecretProvider;
import i2f.core.security.secret.impl.ram.api.*;

/**
 * @author ltb
 * @date 2022/10/19 17:25
 * @desc
 */
public class RamSecretProvider {
    private RamSecretProvider() {
    }

    public static SecretProvider getInstance() {
        return getCustomInstance(false, 1024, 16);
    }

    public static SecretProvider getPkcs1Instance(boolean usePkcs1) {
        return getCustomInstance(usePkcs1, 1024, 16);
    }

    public static SecretProvider getCustomInstance(boolean usePkcs1, int rsaKeyLen, int aesKeyLen) {
        SecretProvider ret = new SecretProvider();
        RsaProvider rsa = new RsaProvider(usePkcs1);
        ret.mineKey = rsa.adaptRsaKeyPair(rsa.makeRsaKey(rsaKeyLen));
        ret.coder = new CharsCoder(aesKeyLen);
        ret.noncer = new CacheNoncer();
        ret.hasher = new Md5Hasher();
        ret.symmetricalEncryptor = new AesSymmEncryptor();
        ret.asymmetricalEncryptor = new RsaAsymEncryptor(rsa);
        return ret;
    }
}
