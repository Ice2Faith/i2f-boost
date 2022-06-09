package i2f.core.jce.md.sha;

import i2f.core.jce.md.BasicMessageDigestor;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc
 */
public class ShaMessageDigestor extends BasicMessageDigestor {
    public ShaMessageDigestor(){
        super(ShaType.SHA1);
    }
    public ShaMessageDigestor(ShaType type) {
        super(type);
    }
}
