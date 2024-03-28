package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum MacAlgorithm {

    HmacMD5("HmacMD5"),
    HmacSHA1("HmacSHA1"),
    HmacSHA224("HmacSHA224"),
    HmacSHA256("HmacSHA256"),
    HmacSHA384("HmacSHA384"),
    HmacSHA512("HmacSHA512"),

    PBEWithHmacMD5("PBEWithHmacMD5"),
    PBEWithHmacSHA1("PBEWithHmacSHA1"),
    PBEWithHmacSHA224("PBEWithHmacSHA224"),
    PBEWithHmacSHA256("PBEWithHmacSHA256"),
    PBEWithHmacSHA384("PBEWithHmacSHA384"),
    PBEWithHmacSHA512("PBEWithHmacSHA512"),
    ;

    private String text;

    MacAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
