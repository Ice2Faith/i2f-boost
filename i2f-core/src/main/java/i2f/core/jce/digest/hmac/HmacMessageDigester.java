package i2f.core.jce.digest.hmac;

import i2f.core.jce.digest.IMessageDigestType;
import i2f.core.jce.digest.MessageDigestUtil;
import i2f.core.jce.digest.exception.MessageDigestException;
import i2f.core.jce.digest.md.MdType;
import i2f.core.jce.digest.std.basic.BasicMessageDigester;

import javax.crypto.Mac;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 18:00
 * @desc 实际上，HMAC是对MD消息摘要算法加上特定的KEY运算之后，更加安全的一种签名
 * 因此，他可以和任意的MD算法结合
 */
public class HmacMessageDigester extends BasicMessageDigester {
    private byte[] key;

    public HmacMessageDigester(byte[] key) {
        super(MdType.MD5);
        this.key = key;
    }

    public HmacMessageDigester(IMessageDigestType type, byte[] key) {
        super(type);
        this.key = key;
    }

    @Override
    public byte[] mds(byte[] data) {
        try {
            return this.mds(new ByteArrayInputStream(data));
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] mds(InputStream is) {
        try {
            Mac mac = MessageDigestUtil.hmacInstance(type, key, providerName);
            return MessageDigestUtil.getHmacs(is, mac);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }
}
