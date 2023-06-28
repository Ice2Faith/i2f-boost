package i2f.core.serialize.adapter;

import i2f.core.serialize.bytes.IBytesObjectSerializer;
import i2f.core.serialize.exception.SerializeException;
import i2f.core.serialize.str.IStringObjectSerializer;

/**
 * 适配器
 * 将StringSerializer转换为BytesSerializer
 */
public class StringBytesSerializerAdapter implements IBytesObjectSerializer {
    private IStringObjectSerializer serializer;
    private String charset = "UTF-8";

    public StringBytesSerializerAdapter(IStringObjectSerializer serializer) {
        this.serializer = serializer;
    }

    public StringBytesSerializerAdapter(IStringObjectSerializer serializer, String charset) {
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
    public byte[] serialize(Object data) {
        String str = serializer.serialize(data);
        return string2bytes(str);
    }

    @Override
    public Object deserialize(byte[] enc) {
        String str = bytes2string(enc);
        return serializer.deserialize(str);
    }

    @Override
    public Object deserialize(byte[] enc, Class<?> clazz) {
        String str = bytes2string(enc);
        return serializer.deserialize(str, clazz);
    }

    @Override
    public Object deserialize(byte[] enc, Object type) {
        String str = bytes2string(enc);
        return serializer.deserialize(str, type);
    }
}
