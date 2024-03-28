package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CipherAlgorithm {

    AES("AES"),
    DES("DES"),
    DESede("DESede"),
    RSA("RSA"),

    AESWrap("AESWrap"),
    ARCFOUR("ARCFOUR"),
    Blowfish("Blowfish"),
    CCM("CCM"),
    DESedeWrap("DESedeWrap"),
    ECIES("ECIES"),
    GCM("GCM"),
    PBEWithMD5AndDES("PBEWithMD5AndDES"),
    PBEWithHmacSHA256AndAES_128("PBEWithHmacSHA256AndAES_128"),
    RC2("RC2"),
    RC4("RC4"),
    RC5("RC5"),
    ;

    private String text;

    CipherAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
