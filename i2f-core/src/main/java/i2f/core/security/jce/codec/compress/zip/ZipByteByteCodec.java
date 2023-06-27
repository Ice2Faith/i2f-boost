package i2f.core.security.jce.codec.compress.zip;

import i2f.core.security.jce.codec.compress.IByteByteCodec;
import i2f.core.security.jce.codec.ex.stream.compress.zip.ZipStreamCodecEx;
import i2f.core.security.jce.codec.exception.CodecException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 9:41
 * @desc
 */
public class ZipByteByteCodec implements IByteByteCodec {
    public static ZipByteByteCodec INSTANCE = new ZipByteByteCodec();
    @Override
    public byte[] encode(byte[] data) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipStreamCodecEx.INSTANCE.encode(bis, bos);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] decode(byte[] enc) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(enc);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ZipStreamCodecEx.INSTANCE.decode(bos, bis);
            return bos.toByteArray();
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

}
