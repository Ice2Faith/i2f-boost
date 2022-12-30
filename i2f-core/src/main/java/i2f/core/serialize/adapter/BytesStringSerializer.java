package i2f.core.serialize.adapter;

import i2f.core.serialize.IBytesSerializer;
import i2f.core.serialize.IStringSerializer;
import i2f.core.serialize.SerializeException;

/**
 * 适配器
 * 将BytesSerializer转换为StringSerializer
 */
public class BytesStringSerializer implements IStringSerializer {
    private IBytesSerializer serializer;
    private String charset = "UTF-8";

    public BytesStringSerializer(IBytesSerializer serializer) {
        this.serializer = serializer;
    }

    public BytesStringSerializer(IBytesSerializer serializer, String charset) {
        this.serializer = serializer;
        this.charset = charset;
    }

    public byte[] string2bytes(String str) {
        try {
            return str.getBytes(charset);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    public String bytes2string(byte[] bytes) {
        try {
            return new String(bytes, charset);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    @Override
    public String serialize(Object obj) {
        return bytes2string(serializer.serialize(obj));
    }

    @Override
    public String serialize(Object obj, boolean formatted) {
        return bytes2string(serializer.serialize(obj, formatted));
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz) {
        return serializer.deserialize(string2bytes(text), clazz);
    }

    @Override
    public <T> T deserialize(String text, Object typeToken) {
        return serializer.deserialize(string2bytes(text), typeToken);
    }
}
