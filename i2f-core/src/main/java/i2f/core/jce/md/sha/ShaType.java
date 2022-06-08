package i2f.core.jce.md.sha;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum ShaType {
    SHA1("SHA-1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");


    private String type;

    private ShaType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
