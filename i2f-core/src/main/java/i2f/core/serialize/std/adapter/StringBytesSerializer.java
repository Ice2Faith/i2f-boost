package i2f.core.serialize.std.adapter;

import i2f.core.serialize.std.IBytesSerializer;
import i2f.core.serialize.std.IStringSerializer;
import i2f.core.serialize.std.SerializeException;

/**
 * 适配器
 * 将StringSerializer转换为BytesSerializer
 */
public class StringBytesSerializer implements IBytesSerializer {
    private IStringSerializer serializer;
    private String charset = "UTF-8";

    public StringBytesSerializer(IStringSerializer serializer) {
        this.serializer = serializer;
    }

    public StringBytesSerializer(IStringSerializer serializer, String charset) {
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
    public byte[] serialize(Object obj) {
        return string2bytes(serializer.serialize(obj));
    }

    @Override
    public byte[] serialize(Object obj, boolean formatted) {
        return string2bytes(serializer.serialize(obj, formatted));
    }

    @Override
    public <T> T deserialize(byte[] text, Class<T> clazz) {
        return serializer.deserialize(bytes2string(text), clazz);
    }

    @Override
    public <T> T deserialize(byte[] text, Object typeToken) {
        return serializer.deserialize(bytes2string(text), typeToken);
    }
}
