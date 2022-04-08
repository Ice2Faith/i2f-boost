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
        if(CheckUtil.isNull(obj)){
            return new byte[]{};
        }
        ByteArrayOutputStream bos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        oos.close();

        return bos.toByteArray();
    }

    public static<T extends Serializable> T bytes2Obj(byte[] bytes) throws IOException, ClassNotFoundException {
        if(CheckUtil.isEmptyArray(bytes)){
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(is);
        T ret = (T) ois.readObject();
        ois.close();
        return ret;
    }

    public static <T extends Serializable> T serializeCopy(T obj) throws IOException, ClassNotFoundException {
        byte[] data = obj2Bytes(obj);
        T ret=bytes2Obj(data);
        return ret;
    }
}
