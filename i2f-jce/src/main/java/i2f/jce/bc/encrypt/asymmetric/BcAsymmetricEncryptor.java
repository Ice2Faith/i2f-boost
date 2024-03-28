package i2f.jce.bc.encrypt.asymmetric;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricEncryptor;
import i2f.jce.jdk.encrypt.asymmetric.AsymmetricType;
import i2f.jce.std.digest.IMessageDigester;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:51
 * @desc
 */
public class BcAsymmetricEncryptor extends AsymmetricEncryptor {
    static {
        BcProvider.registryProvider();
    }

    public BcAsymmetricEncryptor() {
    }

    public BcAsymmetricEncryptor(String algorithmName) {
        super(algorithmName, BcProvider.PROVIDER_NAME);
    }

    public BcAsymmetricEncryptor(String algorithmName, boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithmName, BcProvider.PROVIDER_NAME, noPadding, privateEncrypt, privateKey, publicKey);
    }

    public BcAsymmetricEncryptor(String algorithmName,  boolean noPadding, boolean privateEncrypt, PrivateKey privateKey, PublicKey publicKey, IMessageDigester digester) {
        super(algorithmName, BcProvider.PROVIDER_NAME, noPadding, privateEncrypt, privateKey, publicKey, digester);
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey) {
        super(algorithm, privateKey, publicKey);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm, PrivateKey privateKey, PublicKey publicKey, IMessageDigester digester) {
        super(algorithm, privateKey, publicKey, digester);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair) {
        super(algorithm, keyPair);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public BcAsymmetricEncryptor(AsymmetricType algorithm, KeyPair keyPair, IMessageDigester digester) {
        super(algorithm, keyPair, digester);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public static AsymmetricEncryptor genKeyEncryptor(AsymmetricType algorithm, byte[] keyBytes, String secureRandomAlgorithmName,IMessageDigester digester) throws Exception {
        return new AsymmetricEncryptor(algorithm,
                Encryptor.genKeyPair(algorithm,
                        keyBytes,
                        secureRandomAlgorithmName),
                digester
        );

    }

    @Override
    public String toString() {
        return "BcAsymmetricEncryptor{" +
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
