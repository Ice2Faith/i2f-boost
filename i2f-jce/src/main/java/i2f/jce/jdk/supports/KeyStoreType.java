package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum KeyStoreType {

    jceks("jceks"),
    jks("jks"),
    dks("dks"),
    pkcs11("pkcs11"),
    pkcs12("pkcs12"),
    ;

    private String text;

    KeyStoreType(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
