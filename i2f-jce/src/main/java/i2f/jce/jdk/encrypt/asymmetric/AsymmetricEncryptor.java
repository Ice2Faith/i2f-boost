package i2f.jce.jdk.encrypt.asymmetric;

import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.encrypt.symmetric.SymmetricType;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;
import i2f.jce.std.digest.IMessageDigester;
import i2f.jce.std.encrypt.asymmetric.IAsymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:53
 * @desc
 */
public class AsymmetricEncryptor implements IAsymmetricEncryptor {

    protected String algorithmName;
    protected String providerName;
    protected boolean noPadding;
    protected boolean privateEncrypt;
    protected PrivateKey privateKey;
    protected PublicKey publicKey;
    protected IMessageDigester digester;

    public AsymmetricEncryptor() {
    }

    public AsymmetricEncryptor(String algorithmName, String providerName) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
    }

    public AsymmetricEncryptor(String algorithmName, String providerName, boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.noPadding = noPadding;
        this.privateEncrypt = privateEncrypt;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public AsymmetricEncryptor(String algorithmName, String providerName, boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey, IMessageDigester digester) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.noPadding = noPadding;
        this.privateEncrypt = privateEncrypt;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.digester = digester;
    }

    public AsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public AsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey, IMessageDigester digester) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.digester = digester;
    }

    public AsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    public AsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair, IMessageDigester digester) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.privateEncrypt = algorithm.privateEncrypt();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
        this.digester = digester;
    }

    public static AsymmetricEncryptor genKeyEncryptor(AsymmetricType algorithm, byte[] keyBytes, String secureRandomAlgorithmName,IMessageDigester digester) throws Exception {
            return new AsymmetricEncryptor(algorithm,
                    Encryptor.genKeyPair(algorithm,
                            keyBytes,
                            secureRandomAlgorithmName),
                    digester
                    );

    }

    public Cipher getCipher(boolean encryptMode, boolean isPrivate) throws Exception {
        int mode = encryptMode ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE;
        Cipher cipher = Encryptor.cipherOf(algorithmName, providerName);
        if (encryptMode) {
            if (isPrivate) {
                cipher.init(mode, privateKey);
            } else {
                cipher.init(mode, publicKey);
            }
        } else {
            if (isPrivate) {
                cipher.init(mode, privateKey);
            } else {
                cipher.init(mode, publicKey);
            }
        }
        return cipher;
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true, false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false, true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] privateEncrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true, false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] publicDecrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false, true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        if (digester != null) {
            data = digester.digest(data);
        }
        return privateEncrypt(data);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public boolean isNoPadding() {
        return noPadding;
    }

    public void setNoPadding(boolean noPadding) {
        this.noPadding = noPadding;
    }

    public boolean isPrivateEncrypt() {
        return privateEncrypt;
    }

    public void setPrivateEncrypt(boolean privateEncrypt) {
        this.privateEncrypt = privateEncrypt;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public IMessageDigester getDigester() {
        return digester;
    }

    public void setDigester(IMessageDigester digester) {
        this.digester = digester;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AsymmetricEncryptor that = (AsymmetricEncryptor) o;
        return noPadding == that.noPadding &&
                privateEncrypt == that.privateEncrypt &&
                Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(privateKey, that.privateKey) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(digester, that.digester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, providerName, noPadding, privateEncrypt, privateKey, publicKey, digester);
    }

    @Override
    public String toString() {
        return "AsymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", privateEncrypt=" + privateEncrypt +
                ", privateKey=" + privateKey +
                ", publicKey=" + publicKey +
                ", digester=" + digester +
                '}';
    }
}
