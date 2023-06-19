package i2f.core.jce.digest.sha;

import i2f.core.jce.digest.BasicMessageDigester;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class ShaMessageDigester extends BasicMessageDigester {
    public ShaMessageDigester() {
        super(ShaType.SHA1);
    }

    public ShaMessageDigester(ShaType type) {
        super(type);
    }
}
