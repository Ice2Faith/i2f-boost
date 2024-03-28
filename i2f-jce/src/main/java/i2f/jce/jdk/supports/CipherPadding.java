package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CipherPadding {

    NoPadding("NoPadding"),
    ISO10126Padding("ISO10126Padding"),
    OAEPPadding("OAEPPadding"),
    PKCS1Padding("PKCS1Padding"),
    PKCS5Padding("PKCS5Padding"),
    SSL3Padding("SSL3Padding"),

    OAEPWithMD5AndMGF1Padding("OAEPWithMD5AndMGF1Padding"),
    OAEPWithSHA_512AndMGF1Padding("OAEPWithSHA-512AndMGF1Padding"),
    ;

    private String text;

    CipherPadding(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
