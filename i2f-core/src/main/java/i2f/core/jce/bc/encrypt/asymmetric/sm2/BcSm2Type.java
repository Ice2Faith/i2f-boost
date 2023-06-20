package i2f.core.jce.bc.encrypt.asymmetric.sm2;

import i2f.core.jce.encrypt.IEncryptType;

/**
 * @author ltb
 * @date 2022/6/8 8:50
 * @desc RSA 没有向量一说
 */
public enum BcSm2Type implements IEncryptType {
    /**
     * 默认
     */
    DEFAULT("SM2", false, false);


    private String type;
    private boolean noPadding;
    private boolean requireVector;

    private BcSm2Type(String type, boolean noPadding, boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }

    @Override
    public String type() {
        return type;
    }

    public static final String ALGORITHM_NAME = "EC";
    public static final int[] SECRET_BYTES_LEN = {256};
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
