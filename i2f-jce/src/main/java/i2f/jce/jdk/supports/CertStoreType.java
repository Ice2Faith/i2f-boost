package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CertStoreType {

    Collection("Collection"),
    LDAP("LDAP"),
    ;

    private String text;

    CertStoreType(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
