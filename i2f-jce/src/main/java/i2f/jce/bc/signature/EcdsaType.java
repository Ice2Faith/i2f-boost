package i2f.jce.bc.signature;

import i2f.jce.bc.BcProvider;
import i2f.jce.jdk.signature.SignatureType;

/**
 * @author Ice2Faith
 * @date 2024/3/27 17:33
 * @desc
 */
public enum EcdsaType implements SignatureType {
    /**
     * 默认
     */
    NONEwithECDSA("NONEwithECDSA"),
    RIPEMD160withECDSA("RIPEMD160withECDSA"),
    SHA1withECDSA("SHA1withECDSA"),
    SHA224withECDSA("SHA224withECDSA"),
    SHA256withECDSA("SHA256withECDSA"),
    SHA384withECDSA("SHA384withECDSA"),
    SHA512withECDSA("SHA512withECDSA"),
    ;

    private String type;

    private EcdsaType(String type) {
        this.type = type;
    }


    public static final int[] SECRET_BYTES_LEN = {256};


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
        return "ECDSA";
    }

    @Override
    public int[] secretBytesLen() {
        return SECRET_BYTES_LEN;
    }

}
