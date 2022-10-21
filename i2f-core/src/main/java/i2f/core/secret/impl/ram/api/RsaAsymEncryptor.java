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

    public RsaProvider rsaProvider = new RsaProvider();

    public RsaAsymEncryptor() {
    }

    public RsaAsymEncryptor(RsaProvider rsaProvider) {
        this.rsaProvider = rsaProvider;
    }

    public RsaAsymEncryptor(IKeyPair key, RsaProvider rsaProvider) {
        this.key = key;
        this.rsaProvider = rsaProvider;
    }

    @Override
    public byte[] encryptKey(byte[] data, IKeyPair key) {
        return rsaProvider.encrypt(data, key);
    }

    @Override
    public byte[] decryptKey(byte[] data, IKeyPair key) {
        return rsaProvider.decrypt(data, key);
    }

    @Override
    public byte[] encryptPublicKey(byte[] data, IKeyPair key) {
        return rsaProvider.encryptPublicKey(data, key);
    }

    @Override
    public byte[] decryptPrivateKey(byte[] data, IKeyPair key) {
        return rsaProvider.decryptPrivateKey(data, key);
    }

    @Override
    public byte[] encrypt(byte[] data) {
        return encryptKey(data, key);
    }

    @Override
    public byte[] decrypt(byte[] data) {
        return decryptKey(data, key);
    }
}
