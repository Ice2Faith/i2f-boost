package i2f.core.security.jce.digest.md;

import i2f.core.security.jce.digest.IMessageDigestType;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum MdType implements IMessageDigestType {
    MD2("MD2"),
    MD5("MD5");


    private String type;

    private MdType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
