package i2f.core.serialize;

/**
 * @author ltb
 * @date 2022/4/14 10:11
 * @desc
 */
public interface ISerializer {
    byte[] serialize(Object obj);
    Object deserialize(byte[] data);
    default Object serializeCopy(Object obj){
        byte[] data=serialize(obj);
        return deserialize(data);
    }
}
