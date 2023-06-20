package i2f.core.jce.bc.digest.md;

import i2f.core.jce.digest.IMessageDigestType;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum BcMdType implements IMessageDigestType {
    MD2("MD2"),
    MD5("MD5"),
    MD4("MD4");


    private String type;

    private BcMdType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
