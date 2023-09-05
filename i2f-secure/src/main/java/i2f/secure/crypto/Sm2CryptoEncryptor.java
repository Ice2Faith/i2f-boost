package i2f.secure.crypto;

import i2f.core.check.CheckUtil;
import i2f.core.codec.CodecUtil;
import i2f.core.security.jce.encrypt.std.asymmetric.AsymmetricEncryptor;
import i2f.core.security.jce.encrypt.std.asymmetric.data.AsymKeyPair;
import i2f.core.security.jce.encrypt.std.asymmetric.data.BytesPrivateKey;
import i2f.core.security.jce.encrypt.std.asymmetric.data.BytesPublicKey;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2023/9/4 15:33
 * @desc
 */
public class Sm2CryptoEncryptor implements AsymmetricEncryptor {
    protected int cipherMode = Sm2CryptoUtils.MODE_C1C2C3;
    protected String publicHex;
    protected String privateHex;

    public Sm2CryptoEncryptor() {
        this.cipherMode = Sm2CryptoUtils.MODE_C1C2C3;
    }

    public Sm2CryptoEncryptor(int cipherMode) {
        this.cipherMode = cipherMode;
    }

    public Sm2CryptoEncryptor(int cipherMode, KeyPair keyPair) {
        this.cipherMode = cipherMode;
        setKeyPair(keyPair);
    }

    public Sm2CryptoEncryptor(int cipherMode, byte[] publicBytes, byte[] privateBytes) {
        this.cipherMode = cipherMode;
        if (publicBytes != null) {
            this.publicHex = CodecUtil.toHexString(publicBytes);
        }
        if (privateBytes != null) {
            this.privateHex = CodecUtil.toHexString(privateBytes);
        }
    }

    public Sm2CryptoEncryptor(int cipherMode, String publicHex, String privateHex) {
        this.cipherMode = cipherMode;
        this.publicHex = publicHex;
        this.privateHex = privateHex;
    }

    @Override
    public AsymKeyPair encodeKeyPair(KeyPair keyPair) {
        String publicKey = null;
        String privateKey = null;
        if (keyPair != null) {
            if (keyPair.getPublic() != null) {
                byte[] bts = keyPair.getPublic().getEncoded();
                publicKey = CodecUtil.toHexString(bts);
            }
            if (keyPair.getPrivate() != null) {
                byte[] bts = keyPair.getPrivate().getEncoded();
                privateKey = CodecUtil.toHexString(bts);
            }
        }
        return new AsymKeyPair(publicKey, privateKey);
    }

    @Override
    public KeyPair decodeKeyPair(AsymKeyPair keyPair) {
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        if (keyPair != null) {
            if (!CheckUtil.isEmptyStr(keyPair.getPublicKey())) {
                byte[] bts = CodecUtil.ofHexString(keyPair.getPublicKey());
                try {
                    publicKey = new BytesPublicKey(bts);
                } catch (Exception e) {

                }
            }
            if (!CheckUtil.isEmptyStr(keyPair.getPrivateKey())) {
                byte[] bts = CodecUtil.ofHexString(keyPair.getPrivateKey());
                try {
                    privateKey = new BytesPrivateKey(bts);
                } catch (Exception e) {

                }
            }
        }

        return new KeyPair(publicKey, privateKey);
    }

    @Override
    public void setKeyPair(KeyPair keyPair) {
        if (keyPair != null) {
            if (keyPair.getPublic() != null) {
                byte[] bts = keyPair.getPublic().getEncoded();
                if (bts != null) {
                    this.publicHex = CodecUtil.toHexString(bts);
                }
            }
            if (keyPair.getPrivate() != null) {
                byte[] bts = keyPair.getPrivate().getEncoded();
                if (bts != null) {
                    this.privateHex = CodecUtil.toHexString(bts);
                }
            }
        }
    }

    @Override
    public byte[] publicEncrypt(byte[] data) throws Exception {
        return Sm2CryptoUtils.publicEncrypt(data, publicHex, cipherMode);
    }

    @Override
    public byte[] privateDecrypt(byte[] data) throws Exception {
        return Sm2CryptoUtils.privateDecrypt(data, privateHex, cipherMode);
    }

    @Override
    public byte[] makeSign(byte[] data) throws Exception {
        return Sm2CryptoUtils.makeSign(data, privateHex);
    }

    @Override
    public boolean verifySign(byte[] sign, byte[] data) throws Exception {
        return Sm2CryptoUtils.verifySign(sign, data, publicHex);
    }

    @Override
    public PublicKey getPublicKey(byte[] codes) throws Exception {
        return new BytesPublicKey(codes);
    }

    @Override
    public PrivateKey getPrivateKey(byte[] codes) throws Exception {
        return new BytesPrivateKey(codes);
    }
}
