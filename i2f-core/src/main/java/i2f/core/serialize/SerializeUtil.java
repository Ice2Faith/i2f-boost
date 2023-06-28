package i2f.core.serialize;

import i2f.core.serialize.bytes.jdk.JdkBytesObjectSerializer;

/**
 * @author Ice2Faith
 * @date 2023/6/28 13:47
 * @desc
 */
public class SerializeUtil {
    public static byte[] jdkSerialize(Object obj) {
        return JdkBytesObjectSerializer.INSTANCE.serialize(obj);
    }

    public static Object jdkDeserialize(byte[] data) {
        return JdkBytesObjectSerializer.INSTANCE.deserialize(data);
    }

}
