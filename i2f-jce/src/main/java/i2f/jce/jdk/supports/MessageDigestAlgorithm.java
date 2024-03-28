package i2f.jce.jdk.supports;


/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum MessageDigestAlgorithm {

    MD2("MD2"),
    MD5("MD5"),

    SHA_1("SHA-1"),
    SHA_224("SHA-224"),
    SHA_256("SHA-256"),
    SHA_384("SHA-384"),
    SHA_512("SHA-512"),
    SHA_512_224("SHA-512/224"),
    SHA_512_256("SHA-512/256"),
    ;

    private String text;

    MessageDigestAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
