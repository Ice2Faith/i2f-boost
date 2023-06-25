package i2f.core.security.jce.digest.std;

import i2f.core.security.jce.codec.CodecUtil;

import java.io.InputStream;

/**
 * @author ltb
 * @date 2022/6/9 17:53
 * @desc
 */
public interface IMessageDigester {
    byte[] mds(byte[] data);

    byte[] mds(InputStream is);

    default String mdsText(byte[] data) {
        byte[] bytes = mds(data);
        return CodecUtil.toHexString(bytes);
    }

    default String mdsText(InputStream is) {
        byte[] bytes = mds(is);
        return CodecUtil.toHexString(bytes);
    }
}
