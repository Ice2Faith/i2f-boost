package i2f.core.security.jce.digest.std;

import i2f.core.security.jce.codec.CodecUtil;
import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.ex.stream.IStreamCodecEx;
import i2f.core.security.jce.codec.exception.CodecException;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author ltb
 * @date 2022/6/9 17:53
 * @desc
 */
public interface IMessageDigester extends IByteByteCodec, IStreamCodecEx {
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

    @Override
    default byte[] encode(byte[] data) {
        return mds(data);
    }

    @Override
    default byte[] decode(byte[] enc) {
        throw new UnsupportedOperationException("message digester not support decode.");
    }

    @Override
    default void encode(InputStream data, OutputStream enc) {
        try {
            byte[] bytes = mds(data);
            enc.write(bytes);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    default void decode(OutputStream enc, InputStream data) {
        throw new UnsupportedOperationException("message digester not support decode.");
    }
}
