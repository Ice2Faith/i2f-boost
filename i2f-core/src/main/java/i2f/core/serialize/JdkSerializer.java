package i2f.core.serialize;

import i2f.core.check.CheckUtil;
import i2f.core.serialize.exception.SerializeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author ltb
 * @date 2022/4/14 11:17
 * @desc
 */
public class JdkSerializer implements ISerializer{
    @Override
    public byte[] serialize(Object obj) {
        try{
            if(CheckUtil.isNull(obj)){
                return new byte[]{};
            }
            ByteArrayOutputStream bos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();

            return bos.toByteArray();
        }catch(Exception e){
            throw new SerializeException(e.getMessage(),e);
        }
    }

    @Override
    public Object deserialize(byte[] data) {
        try{
            if(CheckUtil.isEmptyArray(data)){
                return null;
            }
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(is);
            Object ret = ois.readObject();
            ois.close();
            return ret;
        }catch(Exception e){
            throw new SerializeException(e.getMessage(),e);
        }
    }
}
