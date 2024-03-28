package i2f.jce.bc.encrypt.symmetric;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.encrypt.Encryptor;
import i2f.jce.bc.encrypt.symmetric.PbeType;
import i2f.jce.jdk.encrypt.symmetric.SymmetricEncryptor;
import i2f.jce.jdk.encrypt.symmetric.SymmetricType;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

/**
 * @author Ice2Faith
 * @date 2024/3/28 15:50
 * @desc
 */
public class BcSymmetricEncryptor extends SymmetricEncryptor {
    static {
        BcProvider.registryProvider();
    }

    public BcSymmetricEncryptor() {
    }

    public BcSymmetricEncryptor(String algorithmName) {
        super(algorithmName, BcProvider.PROVIDER_NAME);
    }

    public BcSymmetricEncryptor(String algorithmName, boolean noPadding, boolean requireVector, Key key, AlgorithmParameterSpec spec) {
        super(algorithmName, BcProvider.PROVIDER_NAME, noPadding, requireVector, key, spec);
    }

    public BcSymmetricEncryptor(SymmetricType algorithm, Key key, AlgorithmParameterSpec spec) {
        super(algorithm, key, spec);
        this.providerName=BcProvider.PROVIDER_NAME;
    }

    public BcSymmetricEncryptor(SymmetricType algorithm, Key key) {
        super(algorithm, key);
        this.providerName=BcProvider.PROVIDER_NAME;
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


    @Override
    public String toString() {
        return "BcSymmetricEncryptor{" +
                "algorithmName='" + algorithmName + '\'' +
                ", providerName='" + providerName + '\'' +
                ", noPadding=" + noPadding +
                ", requireVector=" + requireVector +
                ", key=" + key +
                ", spec=" + spec +
                '}';
    }
}
