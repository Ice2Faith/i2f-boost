package i2f.core.jce.codec.bytes.base64;

import i2f.core.jce.codec.bytes.IStringByteCodec;

import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:57
 * @desc
 */
public class Base64MimeStringByteCodec implements IStringByteCodec {
    public static Base64MimeStringByteCodec INSTANCE = new Base64MimeStringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base64.getMimeEncoder().encodeToString(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base64.getMimeDecoder().decode(enc);
    }
}
