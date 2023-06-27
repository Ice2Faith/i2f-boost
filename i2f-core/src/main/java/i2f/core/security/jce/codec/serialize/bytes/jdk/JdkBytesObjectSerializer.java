package i2f.core.security.jce.codec.serialize.bytes.jdk;

import i2f.core.security.jce.codec.serialize.bytes.IBytesObjectSerializer;
import i2f.core.serialize.std.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Ice2Faith
 * @date 2023/6/27 17:13
 * @desc
 */
public class JdkBytesObjectSerializer implements IBytesObjectSerializer<Object> {
    public static JdkBytesObjectSerializer INSTANCE = new JdkBytesObjectSerializer();

    @Override
    public byte[] serialize(Object data) {
        if (data == null) {
            return null;
        }
        return jdkSerialize(data);
    }

    @Override
    public Object deserialize(byte[] enc) {
        if (enc == null) {
            return null;
        }
        return jdkDeserialize(enc);
    }

    public static byte[] jdkSerialize(Object obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            return bos.toByteArray();
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    public static <T> T jdkDeserialize(byte[] text) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(text);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object obj = ois.readObject();
            ois.close();
            return (T) obj;
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }
}
