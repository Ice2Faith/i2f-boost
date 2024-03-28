package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum KeyAgreementAlgorithm {

    DiffieHellman("DiffieHellman"),
    ECDH("ECDH"),
    ECMQV("ECMQV"),

    ;

    private String text;

    KeyAgreementAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
