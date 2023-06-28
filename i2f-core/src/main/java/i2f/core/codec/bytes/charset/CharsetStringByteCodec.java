package i2f.core.codec.bytes.charset;

import i2f.core.codec.bytes.IStringByteCodec;
import i2f.core.codec.exception.CodecException;

/**
 * @author Ice2Faith
 * @date 2023/6/20 10:11
 * @desc
 */
public class CharsetStringByteCodec implements IStringByteCodec {
    public static CharsetStringByteCodec UTF8 = new CharsetStringByteCodec("UTF-8");
    public static CharsetStringByteCodec GBK = new CharsetStringByteCodec("GBK");
    public static CharsetStringByteCodec ISO88591 = new CharsetStringByteCodec("ISO-8859-1");

    private String charset = "UTF-8";

    public CharsetStringByteCodec() {
    }

    public CharsetStringByteCodec(String charset) {
        this.charset = charset;
    }


    @Override
    public String encode(byte[] data) {
        try {
            return new String(data, charset);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    @Override
    public byte[] decode(String enc) {
        try {
            return enc.getBytes(charset);
        } catch (Exception e) {
            throw new CodecException(e.getMessage(), e);
        }
    }
}
