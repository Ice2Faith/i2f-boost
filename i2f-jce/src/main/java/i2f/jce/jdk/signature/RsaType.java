package i2f.jce.jdk.signature;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum RsaType implements SignatureType {
    /**
     * 默认
     */
    MD2withRSA("MD2withRSA"),
    MD5withRSA("MD5withRSA"),
    SHA1withRSA("SHA1withRSA"),
    ;

    private String type;

    private RsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {1024, 2048};

    @Override
    public String type() {
        return type;
    }

    @Override
    public String algorithmName() {
        return "RSA";
    }


    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
