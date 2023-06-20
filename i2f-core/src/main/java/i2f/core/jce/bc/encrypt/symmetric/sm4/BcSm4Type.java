package i2f.core.jce.bc.encrypt.symmetric.sm4;

import i2f.core.jce.encrypt.IEncryptType;

/**
 * @author ltb
 * @date 2022/6/8 8:48
 * @desc
 */
public enum BcSm4Type implements IEncryptType {
    /**
     * 默认
     */
    DEFAULT("SM4", false, false),
    /**
     * 有向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     */
    CBC_NO_PADDING("SM4/CBC/NoPadding", true, true),
    /**
     * 有向量加密模式, 不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
     */
    CBC_PKCS5PADDING("SM4/CBC/PKCS5Padding", false, true),
    /**
     * 无向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0
     */
    ECB_NO_PADDING("SM4/ECB/NoPadding", true, false),
    /**
     * 无向量加密模式, 不足8位用余位数补足8位
     */
    ECB_PKCS5PADDING("SM4/ECB/PKCS5Padding", false, false),
    /**
     * 无向量加密模式
     */
    ECB_ISO10126PADDING("SM4/ECB/ISO10126Padding", false, false);

    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private BcSm4Type(String type, boolean noPadding, boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }

    @Override
    public String type() {
        return type;
    }

    public static final String ALGORITHM_NAME = "SM4";
    public static final int[] SECRET_BYTES_LEN = {128}; // 128, 192 or 256 bit
    public static final int[] VECTOR_BYTES_LEN = {128};

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
