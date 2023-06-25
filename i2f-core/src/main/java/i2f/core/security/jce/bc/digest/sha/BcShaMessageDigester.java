package i2f.core.security.jce.bc.digest.sha;

import i2f.core.security.jce.bc.BouncyCastleHolder;
import i2f.core.security.jce.digest.std.basic.BasicMessageDigester;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class BcShaMessageDigester extends BasicMessageDigester {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcShaMessageDigester() {
        super(BcShaType.SHA1);
    }

    public BcShaMessageDigester(BcShaType type) {
        super(type);
    }
}
