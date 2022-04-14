package i2f.core.text;

import i2f.core.serialize.ISerializer;
import i2f.core.text.exception.TextSerializeException;

/**
 * @author ltb
 * @date 2022/4/14 10:11
 * @desc
 */
public interface ITextSerializer extends ISerializer {
    String serializeAsText(Object obj);
    Object deserializeFromText(String str);

    @Override
    default Object serializeCopy(Object obj) {
        String text=serializeAsText(obj);
        return deserializeFromText(text);
    }

    @Override
    default byte[] serialize(Object obj) {
        try{
            return serializeAsText(obj).getBytes("UTF-8");
        }catch(Exception e){
            throw new TextSerializeException(e.getMessage(),e);
        }
    }

    @Override
    default Object deserialize(byte[] data) {
        try{
            return deserializeFromText(new String(data,"UTF-8"));
        }catch(Exception e){
            throw new TextSerializeException(e.getMessage(),e);
        }
    }
}
