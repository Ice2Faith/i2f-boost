package i2f.core.jce.digest;

import i2f.core.jce.digest.exception.MessageDigestException;

import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 17:54
 * @desc
 */
public class BasicMessageDigester implements IMessageDigester {
    protected IMessageDigestType type;

    public BasicMessageDigester(IMessageDigestType type) {
        this.type = type;
    }

    @Override
    public byte[] mds(byte[] data) {
        try {
            return MessageDigestUtil.mds(data, type);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] mds(InputStream is) {
        try {
            return MessageDigestUtil.mds(is, type);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

}
