package i2f.core.digest;

import i2f.core.codec.CodecUtil;
import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.digest.exception.MessageDigestException;
import i2f.core.security.jce.digest.std.IMessageDigester;

import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/29 9:52
 * @desc
 */
public class StringSignatureMessageDigester implements IMessageDigester {
    @Override
    public byte[] mds(byte[] data) {
        String str = CodecUtil.ofUtf8(data);
        String sign = StringSignature.sign(str);
        return CodecUtil.toUtf8(sign);
    }

    @Override
    public byte[] mds(InputStream is) {
        try {
            byte[] data = StreamUtil.readBytes(is, true);
            return this.mds(data);
        } catch (Exception e) {
            throw new MessageDigestException(e.getMessage(), e);
        }
    }

    @Override
    public String mdsText(byte[] data) {
        byte[] bytes = mds(data);
        return CodecUtil.ofUtf8(bytes);
    }

    @Override
    public String mdsText(InputStream is) {
        byte[] bytes = mds(is);
        return CodecUtil.ofUtf8(bytes);
    }
}
