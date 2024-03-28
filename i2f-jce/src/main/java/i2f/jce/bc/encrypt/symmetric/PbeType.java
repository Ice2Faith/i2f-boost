package i2f.jce.bc.encrypt.symmetric;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.encrypt.symmetric.SymmetricType;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum PbeType implements SymmetricType {
    /**
     * 默认
     */
    PBEWithMD5AndDES("PBEWithMD5AndDES", false, true),
    PBEWithMD5AndRC2("PBEWithMD5AndRC2", false, true),
    PBEWithHmacSHA1AndDES("PBEWithHmacSHA1AndDES", false, true),
    PBEWithHmacSHA1AndRC2("PBEWithHmacSHA1AndRC2", false, true),
    PBEWithHmacSHAAndIDEA_CBC("PBEWithHmacSHAAndIDEA-CBC", false, true),
    PBEWithHmacSHAAnd2_KeyTripleDES_CBC("PBEWithHmacSHAAnd2-KeyTripleDES-CBC", false, true),
    PBEWithHmacSHAAnd3_KeyTripleDES_CBC("PBEWithHmacSHAAnd3-KeyTripleDES-CBC", false, true),
    ;

    public static final String[] SUPPORTS_MODE = {
            "CBC",
    };

    public static final String[] SUPPORTS_PADDING = {
            "PKCS5Padding",
            "PKCS7Padding",
            "ISO10126Padding",
            "ZeroBytePadding",
    };


    private String type;
    private boolean noPadding;
    private boolean requireVector;


    private PbeType(String type, boolean noPadding, boolean requireVector) {
        this.type = type;
        this.noPadding = noPadding;
        this.requireVector = requireVector;
    }


    @Override
    public String provider() {
        return BcProvider.PROVIDER_NAME;
    }

    @Override
    public String type() {
        return type;
    }

    public static final int[] SECRET_BYTES_LEN = {0};
    public static final int[] VECTOR_BYTES_LEN = {8};

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
