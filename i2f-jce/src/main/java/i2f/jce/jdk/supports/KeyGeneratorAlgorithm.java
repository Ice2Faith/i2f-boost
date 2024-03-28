package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum KeyGeneratorAlgorithm {

    AES("AES"),
    DES("DES"),
    DESede("DESede"),

    ARCFOUR("ARCFOUR"),
    Blowfish("Blowfish"),
    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA224("HmacSHA224"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),
    RC2("RC2"),
    ;

    private String text;

    KeyGeneratorAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
