package i2f.core.text;

import i2f.core.reflect.convert.ConvertResolver;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.type.TypeResolver;
import i2f.core.serialize.IStringSerializer;
import i2f.core.text.exception.TextSerializeException;

/**
 * @author ltb
 * @date 2022/4/14 10:14
 * @desc
 */
public class FormatTextSerializer implements IStringSerializer {
    private IStringSerializer processor;

    public FormatTextSerializer(IStringSerializer processor) {
        this.processor = processor;
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
        String text = processor.serialize(obj);
        return className+":"+text;
    }

    public Object textDeserialize(String str) {
        int idx = str.indexOf(":");
        if (idx <= 0) {
            throw new TextSerializeException("not valid text serialize format:" + str);
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
                throw new TextSerializeException("not support convert base type:" + className);
            }
        } else {
            val = processor.deserialize(text, clazz);
        }
        return val;
    }

    public Object serializeCopy(Object obj) {
        String text = textSerialize(obj);
        return textDeserialize(text);
    }

    @Override
    public String serialize(Object obj) {
        return textSerialize(obj);
    }

    @Override
    public String serialize(Object obj, boolean formatted) {
        return textSerialize(obj);
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz) {
        return (T) textDeserialize(text);
    }

    @Override
    public <T> T deserialize(String text, Object typeToken) {
        return (T) textDeserialize(text);
    }
}
