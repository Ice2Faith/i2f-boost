package i2f.jce.jdk.encrypt.asymmetric;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum ElGamalType implements AsymmetricType {
    /**
     * 默认
     */
    DEFAULT("ElGamal", false, true),
    /**
     * 默认工作模式, PKCS1Padding模式填充
     */
    NONE_PKCS1PADDING("ElGamal/None/PKCS1Padding", false, true),
    /**
     * 无向量加密模式, PKCS1Padding模式填充
     */
    ECB_PKCS1PADDING("ElGamal/ECB/PKCS1Padding", false, true),
    /**
     * 默认工作模式, PKCS1Padding模式填充
     */
    NONE_PKCS5PADDING("ElGamal/None/PKCS5Padding", false, true),
    /**
     * 无向量加密模式, PKCS5Padding模式填充
     */
    ECB_PKCS5PADDING("ElGamal/ECB/PKCS5Padding", false, true),
    /**
     * 默认工作模式, OAEPPadding模式填充
     */
    NONE_OAEPPadding("ElGamal/None/OAEPPadding", false, true),
    /**
     * 无向量加密模式, OAEPPadding模式填充
     */
    ECB_OAEPPadding("ElGamal/ECB/OAEPPadding", false, true),
    ;

    private String type;
    private boolean noPadding;
    private boolean privateEncrypt;

    private ElGamalType(String type, boolean noPadding, boolean privateEncrypt) {
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
