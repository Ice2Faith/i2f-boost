package i2f.core.jce.md.md;

import i2f.core.jce.md.BasicMessageDigestor;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class MdMessageDigestor extends BasicMessageDigestor {
    public MdMessageDigestor(){
        super(MdType.MD5);
    }
    public MdMessageDigestor(MdType type) {
        super(type);
    }
}
