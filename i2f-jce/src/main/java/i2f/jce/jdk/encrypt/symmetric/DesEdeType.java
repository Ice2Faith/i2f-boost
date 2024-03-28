package i2f.jce.jdk.encrypt.symmetric;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:58
 * @desc
 */
public enum DesEdeType implements SymmetricType {
    /**
     * 默认
     */
    DEFAULT("DESede", false, false),
    /**
     * 有向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     */
    CBC_NO_PADDING("DESede/CBC/NoPadding", true, true),
    /**
     * 有向量加密模式, 不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2};
     * 刚好8位补8位8
     */
    CBC_PKCS5PADDING("DESede/CBC/PKCS5Padding", false, true),
    /**
     * 无向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0
     */
    ECB_NO_PADDING("DESede/ECB/NoPadding", true, false),
    /**
     * 无向量加密模式, 不足8位用余位数补足8位
     */
    ECB_PKCS5PADDING("DESede/ECB/PKCS5Padding", false, false);

    public static final String[] SUPPORTS_MODE = {
            "ECB",
            "CBC",
            "PCBC",
            "CTR",
            "CTS",
            "CFB",
            "CFB8",
            "CFB32",
            "CFB64",
            "CFB128",
            "OFB8",
            "OFB32",
            "OFB64",
            "OFB128",
    };

    public static final String[] SUPPORTS_PADDING = {
            "NoPadding",
            "PKCS5Padding",
            "ISO10126Padding",
    };

    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private DesEdeType(String type, boolean noPadding, boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }

    @Override
    public String type() {
        return type;
    }

    public static final int[] SECRET_BYTES_LEN = {112, 168}; // 112 or 168
    public static final int[] VECTOR_BYTES_LEN = {112,168}; //112
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
