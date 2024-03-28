package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:20
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecureRandom
 */
public enum SecureRandomAlgorithm {
    SHA1PRNG("SHA1PRNG"),
    NativePRNG("NativePRNG"),
    PKCS11("PKCS11"),
    NativePRNGBlocking("NativePRNGBlocking"),
    NativePRNGNonBlocking("NativePRNGNonBlocking"),
    WindowsPRNG("Windows-PRNG"),
    ;

    private String text;

    SecureRandomAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
