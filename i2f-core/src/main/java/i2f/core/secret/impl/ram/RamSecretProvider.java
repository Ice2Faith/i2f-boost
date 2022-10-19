package i2f.core.secret.impl.ram;

import i2f.core.secret.core.SecretProvider;
import i2f.core.secret.impl.ram.api.*;

/**
 * @author ltb
 * @date 2022/10/19 17:25
 * @desc
 */
public class RamSecretProvider {
    private RamSecretProvider() {
    }

    public static SecretProvider getInstance() {
        return getInstance(false, 1024, 16);
    }

    public static SecretProvider getInstance(boolean usePkcs1) {
        return getInstance(usePkcs1, 1024, 16);
    }

    public static SecretProvider getInstance(boolean usePkcs1, int rsaKeyLen, int aesKeyLen) {
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
