package i2f.core.jce.bc.digest.sm;

import i2f.core.jce.digest.IMessageDigestType;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum BcSm3Type implements IMessageDigestType {
    SM3("SM3");


    private String type;

    private BcSm3Type(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
