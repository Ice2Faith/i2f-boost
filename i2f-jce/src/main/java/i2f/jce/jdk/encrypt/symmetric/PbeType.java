package i2f.jce.jdk.encrypt.symmetric;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum PbeType implements SymmetricType {
    /**
     * 默认
     */
    PBEWithHmacSHA1AndAES_128("PBEWithHmacSHA1AndAES_128", false, true),
    PBEWithHmacSHA1AndAES_256("PBEWithHmacSHA1AndAES_256", false, true),
    PBEWithHmacSHA224AndAES_128("PBEWithHmacSHA224AndAES_128", false, true),
    PBEWithHmacSHA224AndAES_256("PBEWithHmacSHA224AndAES_256", false, true),
    PBEWithHmacSHA256AndAES_128("PBEWithHmacSHA256AndAES_128", false, true),
    PBEWithHmacSHA256AndAES_256("PBEWithHmacSHA256AndAES_256", false, true),
    PBEWithHmacSHA384AndAES_128("PBEWithHmacSHA384AndAES_128", false, true),
    PBEWithHmacSHA384AndAES_256("PBEWithHmacSHA384AndAES_256", false, true),
    PBEWithHmacSHA512AndAES_128("PBEWithHmacSHA512AndAES_128", false, true),
    PBEWithHmacSHA512AndAES_256("PBEWithHmacSHA512AndAES_256", false, true),
    PBEWithMD5AndDES("PBEWithMD5AndDES", false, true),
    PBEWithMD5AndTripleDES("PBEWithMD5AndTripleDES", false, true),
    PBEWithSHA1AndDESede("PBEWithSHA1AndDESede", false, true),
    PBEWithSHA1AndRC2_128("PBEWithSHA1AndRC2_128", false, true),
    PBEWithSHA1AndRC2_40("PBEWithSHA1AndRC2_40", false, true),
    PBEWithSHA1AndRC4_128("PBEWithSHA1AndRC4_128", false, true),
    PBEWithSHA1AndRC4_40("PBEWithSHA1AndRC4_40", false, true),

    ;
    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private PbeType(String type, boolean noPadding, boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }

    @Override
    public String type() {
        return type;
    }


    public static final int[] SECRET_BYTES_LEN = {0};
    public static final int[] VECTOR_BYTES_LEN = {64};

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

    @Override
    public int[] vectorBytesLen() {
        return VECTOR_BYTES_LEN;
    }

    @Override
    public boolean noPadding() {
        return noPadding;
    }

    @Override
    public boolean requireVector() {
        return requireVector;
    }

}
