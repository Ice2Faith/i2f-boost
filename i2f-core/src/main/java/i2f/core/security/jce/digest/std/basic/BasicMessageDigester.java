package i2f.core.security.jce.digest.std.basic;

import i2f.core.security.jce.digest.IMessageDigestType;
import i2f.core.security.jce.digest.MessageDigestUtil;
import i2f.core.security.jce.digest.exception.MessageDigestException;
import i2f.core.security.jce.digest.std.IMessageDigester;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 17:54
 * @desc
 */
@Getter
@Setter
public class BasicMessageDigester implements IMessageDigester {
    protected IMessageDigestType type;
    protected String providerName;

    public BasicMessageDigester(IMessageDigestType type) {
        this.type = type;
    }

    @Override
    public byte[] mds(byte[] data) {
        try {
            return MessageDigestUtil.mds(data, type, providerName);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] mds(InputStream is) {
        try {
            return MessageDigestUtil.mds(is, type, providerName);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

}
