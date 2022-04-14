package i2f.core.serialize;

import i2f.core.annotations.remark.Author;
import i2f.core.check.CheckUtil;

import java.io.*;

/**
 * @author ltb
 * @date 2022/3/19 15:23
 * @desc
 */
@Author("i2f")
public class SerializeUtil {
    private static volatile ISerializer serializer=new JdkSerializer();
    public static byte[] objs2Bytes(Object ... objs) throws IOException {
        if(CheckUtil.isEmptyArray(objs)){
            return new byte[]{};
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        for(Object item : objs){
            oos.writeObject(item);
        }
        oos.flush();
        oos.close();

        return bos.toByteArray();
    }

    public static<T extends Serializable> byte[] obj2Bytes(T obj) throws IOException {
        return serializer.serialize(obj);
    }

    public static<T extends Serializable> T bytes2Obj(byte[] bytes) throws IOException, ClassNotFoundException {
        return (T)serializer.deserialize(bytes);
    }

    public static <T extends Serializable> T serializeCopy(T obj) throws IOException, ClassNotFoundException {
        return (T)serializer.serializeCopy(obj);
    }
}
