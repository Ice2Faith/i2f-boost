package i2f.core.security.jce.codec.bytes.base64;

import i2f.core.io.stream.StreamUtil;
import i2f.core.security.jce.codec.bytes.IStringByteCodec;

import java.io.File;
import java.io.IOException;
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

    public static String imageToBase64(File file, boolean prefix) throws IOException {
        String webPrefix = "data:image/*;base64,";
        byte[] data = StreamUtil.readBytes(file);
        String b64 = INSTANCE.encode(data);
        if (prefix) {
            return webPrefix + b64;
        }
        return b64;
    }

    public static void base64ToFile(String base64, File file) throws IOException {
        String webPrefix = "data:image/*;base64,";
        if (base64.startsWith(webPrefix)) {
            base64 = base64.substring(webPrefix.length());
        }
        byte[] data = INSTANCE.decode(base64);
        StreamUtil.writeBytes(data, file);
    }
}
