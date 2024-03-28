package i2f.jce.jdk.encrypt.asymmetric;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum RsaType implements AsymmetricType {
    /**
     * 默认
     */
    DEFAULT("RSA", false, true),
    /**
     * 默认工作模式, PKCS1Padding模式填充
     */
    NONE_PKCS1PADDING("RSA/None/PKCS1Padding", false, true),
    /**
     * 无向量加密模式, PKCS1Padding模式填充
     */
    ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding", false, true),
    /**
     * 默认工作模式, PKCS1Padding模式填充
     */
    NONE_PKCS5PADDING("RSA/None/PKCS5Padding", false, true),
    /**
     * 无向量加密模式, PKCS5Padding模式填充
     */
    ECB_PKCS5PADDING("RSA/ECB/PKCS5Padding", false, true),
    /**
     * 默认工作模式, OAEPPadding模式填充
     */
    NONE_OAEPPadding("RSA/None/OAEPPadding", false, true),
    /**
     * 无向量加密模式, OAEPPadding模式填充
     */
    ECB_OAEPPadding("RSA/ECB/OAEPPadding", false, true),
    /**
     * 无向量加密模式, SHA-1摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA1_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", false, true),
    /**
     * 无向量加密模式, SHA-256摘要 + MGF1方式填充
     */
    ECB_OAEP_WITH_SHA256_AND_MGF_1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding", false, true);


    public static final String[] SUPPORTS_MODE = {
            "ECB",
    };

    public static final String[] SUPPORTS_PADDING = {
            "NoPadding",
            "PKCS1Padding",
            "OAEPWITHMD5AndMGF1Pading",
            "OAEPWITHSHA1AndMGF1Pading",
            "OAEPWITHSHA256AndMGF1Pading",
            "OAEPWITHSHA384AndMGF1Pading",
            "OAEPWITHSHA512AndMGF1Pading",
    };

    private String type;
    private boolean noPadding;
    private boolean privateEncrypt;

    private RsaType(String type, boolean noPadding, boolean privateEncrypt) {
        this.type = type;
        this.noPadding = noPadding;
        this.privateEncrypt = privateEncrypt;
    }

    @Override
    public String type() {
        return type;
    }

    public static final int[] SECRET_BYTES_LEN = {1024, 2048}; // 1024，2048

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

    @Override
    public boolean noPadding() {
        return noPadding;
    }

    @Override
    public boolean privateEncrypt() {
        return privateEncrypt;
    }


}
