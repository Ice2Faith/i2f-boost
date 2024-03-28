package i2f.jce.jdk.signature;

import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.std.signature.ISignatureSigner;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/28 10:56
 * @desc
 */
public class SignatureSigner implements ISignatureSigner {

    protected String algorithmName;
    protected String providerName;
    protected PublicKey publicKey;
    protected PrivateKey privateKey;

    public SignatureSigner() {
    }

    public SignatureSigner(String algorithmName, String providerName) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
    }

    public SignatureSigner(String algorithmName, String providerName, PublicKey publicKey, PrivateKey privateKey) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    public SignatureSigner(SignatureType algorithm, PublicKey publicKey, PrivateKey privateKey){
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.publicKey = publicKey;
        this.privateKey = privateKey;
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


    public Signature getSignature(boolean verify) throws Exception {
        Signature signature = Encryptor.signatureOf(algorithmName, providerName);
        if (verify) {
            signature.initVerify(publicKey);
        } else {
            signature.initSign(privateKey);
        }
        return signature;
    }

    @Override
    public byte[] sign(byte[] data) throws Exception {
        Signature signature = getSignature(false);
        signature.update(data);
        return signature.sign();
    }

    @Override
    public boolean verify(byte[] sign, byte[] data) throws Exception {
        Signature signature = getSignature(true);
        signature.update(data);
        return signature.verify(sign);
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

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignatureSigner that = (SignatureSigner) o;
        return Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(publicKey, that.publicKey) &&
                Objects.equals(privateKey, that.privateKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, providerName, publicKey, privateKey);
    }

    @Override
    public String toString() {
        return "SignatureSigner{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", publicKey=" + publicKey +
                ", privateKey=" + privateKey +
                '}';
    }
}
