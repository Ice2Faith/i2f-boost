package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CipherMode {

    NONE("NONE"),
    CBC("CBC"),
    CCM("CCM"),
    CFB("CFB"),
    CFB8("CFB8"),
    OFB32("OFB32"),

    CTR("CTR"),
    CTS("CTS"),
    ECB("ECB"),
    GCM("GCM"),
    PCBC("PCBC"),
    ;

    private String text;

    CipherMode(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
