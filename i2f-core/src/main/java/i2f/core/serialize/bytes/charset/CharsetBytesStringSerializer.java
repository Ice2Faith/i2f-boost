package i2f.core.serialize.bytes.charset;

import i2f.core.codec.bytes.charset.CharsetStringByteCodec;
import i2f.core.serialize.bytes.IBytesStringSerializer;

/**
 * @author Ice2Faith
 * @date 2023/6/28 9:20
 * @desc
 */
public class CharsetBytesStringSerializer implements IBytesStringSerializer {
    public static CharsetBytesStringSerializer UTF8 = new CharsetBytesStringSerializer("UTF-8");
    public static CharsetBytesStringSerializer GBK = new CharsetBytesStringSerializer("GBK");
    public static CharsetBytesStringSerializer ISO88591 = new CharsetBytesStringSerializer("ISO-8859-1");

    private CharsetStringByteCodec codec = CharsetStringByteCodec.UTF8;

    public CharsetBytesStringSerializer() {
    }

    public CharsetBytesStringSerializer(String charset) {
        this.codec = new CharsetStringByteCodec(charset);
    }

    public CharsetBytesStringSerializer(CharsetStringByteCodec codec) {
        this.codec = codec;
    }

    @Override
    public byte[] serialize(String data) {
        return codec.decode(data);
    }

    @Override
    public String deserialize(byte[] enc) {
        return codec.encode(enc);
    }
}
