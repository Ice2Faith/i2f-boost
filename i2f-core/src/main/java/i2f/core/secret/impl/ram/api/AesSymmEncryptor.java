package i2f.core.secret.impl.ram.api;

import i2f.core.secret.api.encrypt.ISymmetricalEncryptor;
import i2f.core.secret.exception.SecretException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author ltb
 * @date 2022/10/19 17:24
 * @desc
 */
public class AesSymmEncryptor implements ISymmetricalEncryptor {
    public byte[] key;

    public AesSymmEncryptor() {
    }

    public AesSymmEncryptor(byte[] key) {
        this.key = key;
    }

    public Cipher getAes(byte[] key, int mode) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
            cipher.init(mode, keySpec);
            return cipher;
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    @Override
    public byte[] encryptKey(byte[] data, byte[] key) {
        try {
            Cipher cipher = getAes(key, Cipher.ENCRYPT_MODE);
            byte[] sdata = cipher.doFinal(data);
            return sdata;
        } catch (Exception e) {
            throw new SecretException(e);
        }
    }

    @Override
    public byte[] decryptKey(byte[] data, byte[] key) {
        try {
            Cipher cipher = getAes(key, Cipher.DECRYPT_MODE);
            byte[] sdata = cipher.doFinal(data);
            return sdata;
        } catch (Exception e) {
            throw new SecretException(e);
        }
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
