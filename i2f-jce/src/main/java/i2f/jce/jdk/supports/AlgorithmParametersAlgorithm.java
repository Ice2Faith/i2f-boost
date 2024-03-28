package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum AlgorithmParametersAlgorithm {

    AES("AES"),
    DES("DES"),
    DESede("DESede"),

    Blowfish("Blowfish"),
    DiffieHellman("DiffieHellman"),
    DSA("DSA"),
    OAEP("OAEP"),
    PBE("PBE"),
    RC2("RC2"),
    RSASSA_PSS("RSASSA-PSS"),

    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithHmacSHA256AndAES_128("PBEWithHmacSHA256AndAES_128"),

    ;

    private String text;

    AlgorithmParametersAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
