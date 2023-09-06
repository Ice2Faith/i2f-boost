package i2f.core.security.jce.sm.digest;

import com.antherd.smcrypto.sm3.Sm3;
import i2f.core.codec.CodecUtil;
import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.digest.std.IMessageDigester;

import java.io.InputStream;

/**
 * @author Ice2Faith
 * @date 2023/9/6 14:23
 * @desc
 */
public class Sm3CryptoMessageDigester implements IMessageDigester {
    @Override
    public byte[] mds(byte[] data) {
        String str = CodecUtil.ofUtf8(data);
        String enc = Sm3.sm3(str);
        return CodecUtil.toUtf8(enc);
    }

    @Override
    public byte[] mds(InputStream is) {
        try {
            byte[] data = StreamUtil.readBytes(is, true);
            return mds(data);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e.getMessage(), e);
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
