package i2f.core.jce.bc.digest.sha;

import i2f.core.jce.digest.IMessageDigestType;

/**
 * @author ltb
 * @date 2022/6/8 8:52
 * @desc
 */
public enum BcShaType implements IMessageDigestType {
    SHA1("SHA-1"),
    SHA224("SHA-224"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512");


    private String type;

    private BcShaType(String type) {
        this.type = type;
    }

    @Override
    public String type() {
        return type;
    }
}
