package i2f.core.jce.md.hmac;

import i2f.core.jce.md.BasicMessageDigestor;
import i2f.core.jce.md.IMdType;
import i2f.core.jce.md.md.MdType;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc 实际上，HMAC是对MD消息摘要算法加上特定的KEY运算之后，更加安全的一种签名
 * 因此，他可以和任意的MD算法结合
 */
public class HmacMessageDigestor extends BasicMessageDigestor {
    private byte[] key;

    public HmacMessageDigestor(byte[] key) {
        super(MdType.MD5);
        this.key = key;
    }

    public HmacMessageDigestor(IMdType type, byte[] key) {
        super(type);
        this.key = key;
    }

    @Override
    public byte[] mds(InputStream is) throws Exception {
        String hmacName = "Hmac" + type.type().replaceAll("-", "");
        SecretKey skey = new SecretKeySpec(key, hmacName);
        Mac mac = Mac.getInstance(hmacName);
        mac.init(skey);
        byte[] buf = new byte[16];
        int len = 0;
        while ((len = is.read(buf)) > 0) {
            mac.update(buf, 0, len);
        }
        return mac.doFinal();
    }
}
