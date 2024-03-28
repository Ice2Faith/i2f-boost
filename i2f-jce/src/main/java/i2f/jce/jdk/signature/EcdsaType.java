package i2f.jce.jdk.signature;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum EcdsaType implements SignatureType {
    /**
     * 默认
     */
    NONEwithECDSA("NONEwithECDSA"),
    SHA1withECDSA("SHA1withECDSA"),
    SHA256withECDSA("SHA256withECDSA"),
    SHA384withECDSA("SHA384withECDSA"),
    SHA512withECDSA("SHA512withECDSA"),
    ;

    private String type;

    private EcdsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {256};

    @Override
    public String type() {
        return type;
    }

    @Override
    public String algorithmName() {
        return "EC";
    }

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
