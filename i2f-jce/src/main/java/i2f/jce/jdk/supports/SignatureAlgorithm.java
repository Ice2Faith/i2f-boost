package i2f.jce.jdk.supports;

/**
 * @author Ice2Faith
 * @date 2024/3/27 14:31
 * @desc https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public enum SignatureAlgorithm {

    NONEwithRSA("NONEwithRSA"),

    MD2withRSA("MD2withRSA"),
    MD5withRSA("MD5withRSA"),

    SHA1withRSA("SHA1withRSA"),
    SHA224withRSA("SHA224withRSA"),
    SHA256withRSA("SHA256withRSA"),
    SHA384withRSA("SHA384withRSA"),
    SHA512withRSA("SHA512withRSA"),
    SHA512_224withRSA("SHA512/224withRSA"),
    SHA512_256withRSA("SHA512/256withRSA"),

    RSASSA_PSS("RSASSA-PSS"),

    NONEwithDSA("NONEwithDSA"),

    SHA1withDSA("SHA1withDSA"),
    SHA224withDSA("SHA224withDSA"),
    SHA256withDSA("SHA256withDSA"),
    SHA384withDSA("SHA384withDSA"),
    SHA512withDSA("SHA512withDSA"),

    NONEwithECDSA("NONEwithECDSA"),
    SHA1withECDSA("SHA1withECDSA"),
    SHA224withECDSA("SHA224withECDSA"),
    SHA256withECDSA("SHA256withECDSA"),
    SHA384withECDSA("SHA384withECDSA"),
    SHA512withECDSA("SHA512withECDSA"),


    MD5withRSAandMGF1("MD5withRSAandMGF1"),
    ;

    private String text;

    SignatureAlgorithm(String text) {
        this.text = text;
    }

    public String text() {
        return this.text;
    }
}
