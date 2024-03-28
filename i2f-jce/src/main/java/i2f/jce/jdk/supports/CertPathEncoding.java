package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum CertPathEncoding {

    PKCS7("PKCS7"),
    PkiPath("PkiPath"),
    ;

    private String text;

    CertPathEncoding(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
