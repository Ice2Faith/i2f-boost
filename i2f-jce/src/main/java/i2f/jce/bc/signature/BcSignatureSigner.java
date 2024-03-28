package i2f.jce.bc.signature;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.signature.SignatureSigner;
import i2f.jce.jdk.signature.SignatureType;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:46
 * @desc
 */
public class BcSignatureSigner extends SignatureSigner {
    static {
        BcProvider.registryProvider();
    }

    public BcSignatureSigner() {
    }

    public BcSignatureSigner(String algorithmName) {
        super(algorithmName, BcProvider.PROVIDER_NAME);
    }

    public BcSignatureSigner(String algorithmName, PublicKey publicKey, PrivateKey privateKey) {
        super(algorithmName, BcProvider.PROVIDER_NAME, publicKey, privateKey);
    }

    public BcSignatureSigner(SignatureType algorithm, PublicKey publicKey, PrivateKey privateKey) {
        super(algorithm, publicKey, privateKey);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public static KeyPair getSignatureKeyPair(SignatureType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        return Encryptor.genKeyPair(algorithm.algorithmName(),
                algorithm.provider(),
                secretBytes,algorithm.secretBytesLen()[0],secureRandomAlgorithmName);
    }

    public static SignatureSigner genKeySignatureSigner(SignatureType algorithm, byte[] secretBytes, String secureRandomAlgorithmName) throws Exception {
        KeyPair keyPair = getSignatureKeyPair(algorithm, secretBytes, secureRandomAlgorithmName);
        return new SignatureSigner(algorithm, keyPair.getPublic(), keyPair.getPrivate());
    }

    @Override
    public String toString() {
        return "BcSignatureSigner{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                '}';
    }
}
