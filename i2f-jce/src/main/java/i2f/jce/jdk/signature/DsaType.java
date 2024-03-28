package i2f.jce.jdk.signature;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum DsaType implements SignatureType {
    /**
     * 默认
     */
    SHA1withDSA("SHA1withDSA"),
    ;

    private String type;

    private DsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {1024, 2048};

    @Override
    public String type() {
        return type;
    }

    @Override
    public String algorithmName() {
        return "DSA";
    }

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
