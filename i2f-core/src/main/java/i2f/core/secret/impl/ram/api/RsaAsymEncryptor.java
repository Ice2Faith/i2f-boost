package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.encrypt.IAsymmetricalEncryptor;
import i2f.core.secret.api.key.IKeyPair;

/**
 * @author ltb
 * @date 2022/10/19 17:24
 * @desc
 */
public class RsaAsymEncryptor implements IAsymmetricalEncryptor {
    public IKeyPair key;

    public i2f.core.secret.impl.ram.api.RsaProvider rsaProvider = new i2f.core.secret.impl.ram.api.RsaProvider();

    public RsaAsymEncryptor() {
    }

    public RsaAsymEncryptor(i2f.core.secret.impl.ram.api.RsaProvider rsaProvider) {
        this.rsaProvider = rsaProvider;
    }

    public RsaAsymEncryptor(IKeyPair key, i2f.core.secret.impl.ram.api.RsaProvider rsaProvider) {
        this.key = key;
        this.rsaProvider = rsaProvider;
    }

    @Override
    public byte[] encrypt(byte[] data, IKeyPair key) {
        return rsaProvider.encrypt(data, key);
    }

    @Override
    public byte[] decrypt(byte[] data, IKeyPair key) {
        return rsaProvider.decrypt(data, key);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return encrypt(data, key);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return decrypt(data, key);
    }
}
