package i2f.core.security.jce.codec.bytes.base64;

import i2f.core.security.jce.codec.bytes.IStringByteCodec;

import java.util.Base64;

/**
 * @author Ice2Faith
 * @date 2023/6/19 15:57
 * @desc
 */
public class Base64StringByteCodec implements IStringByteCodec {
    public static Base64StringByteCodec INSTANCE = new Base64StringByteCodec();

    @Override
    public String encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    @Override
    public byte[] decode(String enc) {
        return Base64.getDecoder().decode(enc);
    }
}
