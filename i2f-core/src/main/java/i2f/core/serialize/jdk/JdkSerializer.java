package i2f.core.serialize.jdk;

import i2f.core.serialize.std.IBytesSerializer;
import i2f.core.serialize.std.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class JdkSerializer implements IBytesSerializer {

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

    @Override
    public byte[] serialize(Object obj) {
        return jdkSerialize(obj);
    }

    @Override
    public byte[] serialize(Object obj, boolean formatted) {
        return jdkSerialize(obj);
    }

    @Override
    public <T> T deserialize(byte[] text, Class<T> clazz) {
        return jdkDeserialize(text);
    }

    @Override
    public <T> T deserialize(byte[] text, Object typeToken) {
        return jdkDeserialize(text);
    }
}
