package i2f.jce.jdk.encrypt.symmetric;

import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.jdk.supports.SecureRandomAlgorithm;
import i2f.jce.std.encrypt.symmetric.ISymmetricEncryptor;

import javax.crypto.Cipher;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/27 13:53
 * @desc
 */
public class SymmetricEncryptor implements ISymmetricEncryptor {

    protected String algorithmName;
    protected String providerName;
    protected boolean noPadding;
    protected boolean requireVector;
    protected Key key;
    protected AlgorithmParameterSpec spec;

    public SymmetricEncryptor() {
    }

    public SymmetricEncryptor(String algorithmName, String providerName) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
    }

    public SymmetricEncryptor(String algorithmName, String providerName, boolean noPadding, boolean requireVector, Key key, AlgorithmParameterSpec spec) {
        this.algorithmName = algorithmName;
        this.providerName = providerName;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
        this.key = key;
        this.spec = spec;
    }

    public SymmetricEncryptor(SymmetricType algorithm, Key key, AlgorithmParameterSpec spec) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.requireVector = algorithm.requireVector();
        this.key = key;
        this.spec = spec;
    }

    public SymmetricEncryptor(SymmetricType algorithm, Key key) {
        this.algorithmName = algorithm.type();
        this.providerName = algorithm.provider();
        this.noPadding = algorithm.noPadding();
        this.requireVector = algorithm.requireVector();
        this.key = key;
    }

    public static SymmetricEncryptor genKeyEncryptor(SymmetricType algorithm, byte[] keyBytes, byte[] vectorBytes, String secureRandomAlgorithmName) throws Exception {
        if (algorithm.requireVector()) {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genSecretKey(algorithm,
                            keyBytes,
                            secureRandomAlgorithmName),
                    Encryptor.genParameterSpec(algorithm,
                            vectorBytes,
                            secureRandomAlgorithmName));
        } else {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genSecretKey(algorithm,
                            keyBytes,
                            secureRandomAlgorithmName));
        }
    }

    public static SymmetricEncryptor genPbeKeyEncryptor(PbeType algorithm, byte[] keyBytes, byte[] vectorBytes, int iterationCount, String secureRandomAlgorithmName) throws Exception {
        if (algorithm.requireVector()) {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genPbeSecretKey(algorithm.algorithmName(),
                            null,
                            keyBytes,
                            secureRandomAlgorithmName),
                    Encryptor.genPbeParameterSpec(algorithm.algorithmName(),
                            vectorBytes,
                            algorithm.vectorBytesLen()[0],
                            secureRandomAlgorithmName,
                            iterationCount));
        } else {
            return new SymmetricEncryptor(algorithm,
                    Encryptor.genPbeSecretKey(algorithm.algorithmName(),
                            null,
                            keyBytes,
                            secureRandomAlgorithmName));
        }
    }


    public Cipher getCipher(boolean encrypt) throws Exception {
        return Encryptor.getCipher(algorithmName, providerName, encrypt, key, requireVector ? spec : null);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(true);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = getCipher(false);
        if (noPadding) {
            data = Encryptor.handleNoPaddingEncryptFormat(cipher, data);
        }
        return Encryptor.work(data, cipher);
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

    public boolean isRequireVector() {
        return requireVector;
    }

    public void setRequireVector(boolean requireVector) {
        this.requireVector = requireVector;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public AlgorithmParameterSpec getSpec() {
        return spec;
    }

    public void setSpec(AlgorithmParameterSpec spec) {
        this.spec = spec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SymmetricEncryptor that = (SymmetricEncryptor) o;
        return noPadding == that.noPadding &&
                requireVector == that.requireVector &&
                Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(providerName, that.providerName) &&
                Objects.equals(key, that.key) &&
                Objects.equals(spec, that.spec);
    }

    @Override
    public int hashCode() {
        return Objects.hash(algorithmName, providerName, noPadding, requireVector, key, spec);
    }

    @Override
    public String toString() {
        return "SymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", requireVector=" + requireVector +
                ", key=" + key +
                ", spec=" + spec +
                '}';
    }
}
