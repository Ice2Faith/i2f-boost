package i2f.core.jce.bc.digest.sm;

import i2f.core.jce.bc.BouncyCastleHolder;
import i2f.core.jce.digest.std.basic.BasicMessageDigester;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class BcSm3MessageDigester extends BasicMessageDigester {
    {
        BouncyCastleHolder.registry();
        providerName = BouncyCastleHolder.PROVIDER_NAME;
    }

    public BcSm3MessageDigester() {
        super(BcSm3Type.SM3);
    }

    public BcSm3MessageDigester(BcSm3Type type) {
        super(type);
    }
}
