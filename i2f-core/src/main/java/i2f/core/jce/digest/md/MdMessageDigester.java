package i2f.core.jce.digest.md;

import i2f.core.jce.digest.std.basic.BasicMessageDigester;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class MdMessageDigester extends BasicMessageDigester {
    public MdMessageDigester() {
        super(MdType.MD5);
    }

    public MdMessageDigester(MdType type) {
        super(type);
    }
}
