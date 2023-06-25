package i2f.core.security.jce.bc.digest.md;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.digest.std.basic.BasicMessageDigester;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class BcMdMessageDigester extends BasicMessageDigester {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcMdMessageDigester() {
        super(BcMdType.MD5);
    }

    public BcMdMessageDigester(BcMdType type) {
        super(type);
    }
}
