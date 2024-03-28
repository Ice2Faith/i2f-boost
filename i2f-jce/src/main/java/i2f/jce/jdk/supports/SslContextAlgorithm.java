package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum SslContextAlgorithm {

    SSL("SSL"),
    SSLv2("SSLv2"),
    SSLv3("SSLv3"),

    TLS("TLS"),
    TLSv1("TLSv1"),
    TLSv1_1("TLSv1.1"),
    TLSv1_2("TLSv1.2"),
    ;

    private String text;

    SslContextAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
