package i2f.core.jce.encrypt.aes;

import i2f.core.jce.encrypt.IEncryptType;

/**
 * @author ltb
 * @date 2022/6/8 8:48
 * @desc
 */
public enum AesType implements IEncryptType {

    /**
     * 有向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0, 如{65,65,65,0,0,0,0,0}
     */
    CBC_NO_PADDING("AES/CBC/NoPadding",true,true),
    /**
     * 有向量加密模式, 不足8位用余位数补足8位, 如{65,65,65,5,5,5,5,5}或{97,97,97,97,97,97,2,2}; 刚好8位补8位8
     */
    CBC_PKCS5PADDING("AES/CBC/PKCS5Padding",false,true),
    /**
     * 无向量加密模式, 不足8位用0补足8位, 需代码给加密内容添加0
     */
    ECB_NO_PADDING("AES/ECB/NoPadding",true,false),
    /**
     * 无向量加密模式, 不足8位用余位数补足8位
     */
    ECB_PKCS5PADDING("AES/ECB/PKCS5Padding",false,false);

    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private AesType(String type,boolean noPadding,boolean requireVector) {
        this.type = type;
        this.noPadding=noPadding;
        this.requireVector=requireVector;
    }

    @Override
    public String type() {
        return type;
    }

    public static final String ALGORITHM_NAME = "AES";
    public static final int SECRET_BYTES_LEN = 128; // 128, 192 or 256
    public static final int VECTOR_BYTES_LEN = 128;

    @Override
    public String algorithmName() {
        return ALGORITHM_NAME;
    }

    @Override
    public int secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

    @Override
    public int vectorBytesLen() {
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
