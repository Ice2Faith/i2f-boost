package i2f.core.serialize.str.text;

import i2f.core.reflection.reflect.convert.ConvertResolver;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.reflection.reflect.type.TypeResolver;
import i2f.core.serialize.str.IStringObjectSerializer;
import i2f.core.serialize.str.text.exception.FormatTextSerializeException;

/**
 * @author ltb
 * @date 2022/4/14 10:14
 * @desc
 */
public class FormatTextSerializer implements IStringObjectSerializer {
    private IStringObjectSerializer serializer;

    public FormatTextSerializer(IStringObjectSerializer serializer) {
        this.serializer = serializer;
    }

    public String textSerialize(Object obj) {
        if (obj == null) {
            return "null:";
        }
        Class clazz = obj.getClass();
        String className = clazz.getName();
        if (className.startsWith("java.lang.")) {
            className = "$" + className.substring("java.lang.".length());
        }
        if (TypeResolver.isBaseType(clazz)) {
            return className + ":" + obj;
        }
        String text = serializer.serialize(obj);
        return className+":"+text;
    }

    public Object textDeserialize(String str) {
        int idx = str.indexOf(":");
        if (idx <= 0) {
            throw new FormatTextSerializeException("not valid text serialize format:" + str);
        }
        String className = str.substring(0, idx);
        if (className.startsWith("$")) {
            className = "java.lang." + className.substring(1);
        }
        String text = str.substring(idx + 1);
        if ("null".equals(className)) {
            return null;
        }
        Object val=null;
        Class clazz= ReflectResolver.getClazz(className);
        if(TypeResolver.isBaseType(clazz)){
            if (ConvertResolver.isValueConvertible(text, clazz)) {
                val = ConvertResolver.tryConvertible(text, clazz);
            } else {
                throw new FormatTextSerializeException("not support convert base type:" + className);
            }
        } else {
            val = serializer.deserialize(text, clazz);
        }
        return val;
    }

    public Object serializeCopy(Object obj) {
        String text = textSerialize(obj);
        return textDeserialize(text);
    }

    @Override
    public String serialize(Object data) {
        return textSerialize(data);
    }

    @Override
    public Object deserialize(String enc) {
        return textDeserialize(enc);
    }
}
