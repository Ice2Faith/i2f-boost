package i2f.jce.bc.signature;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.signature.SignatureType;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum DsaType implements SignatureType {
    /**
     * 默认
     */
    SHA224withDSA("SHA224withDSA"),
    SHA256withDSA("SHA256withDSA"),
    SHA384withDSA("SHA384withDSA"),
    SHA512withDSA("SHA512withDSA"),
    ;

    private String type;

    private DsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {1024, 2048};


    @Override
    public String provider() {
        return BcProvider.PROVIDER_NAME;
    }

    @Override
    public String type() {
        return type;
    }

    @Override
    public String algorithmName() {
        return "DSA";
    }

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
