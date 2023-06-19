package i2f.core.jce.encrypt.asymmetric.rsa;

import i2f.core.jce.encrypt.IEncryptType;

/**
 * @author ltb
 * @date 2022/6/8 8:50
 * @desc RSA 没有向量一说
 */
public enum RsaType implements IEncryptType {
    /**
     * 无向量加密模式, PKCS1Padding模式填充
     */
    ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding",false,false),
    /**
     * 无向量加密模式, SHA-1摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA1_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding",false,false),
    /**
     * 无向量加密模式, SHA-256摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA256_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",false,false);



    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private RsaType(String type,boolean noPadding,boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }

    @Override
    public String type() {
        return type;
    }

    public static final String ALGORITHM_NAME = "RSA";
    public static final int[] SECRET_BYTES_LEN = {1024, 2048}; // 2048
    public static final int[] VECTOR_BYTES_LEN = {0};

    @Override
    public String algorithmName() {
        return ALGORITHM_NAME;
    }

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
