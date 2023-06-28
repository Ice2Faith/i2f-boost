package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import i2f.core.serialize.exception.SerializeException;
import i2f.core.serialize.str.IStringObjectSerializer;


public abstract class AbsJacksonSerializer implements IStringObjectSerializer {

    public abstract ObjectMapper getMapper();

    @Override
    public String serialize(Object obj) {
        return serialize(obj, false);
    }

    public String serialize(Object obj, boolean formatted) {
        try {
            ObjectWriter writer = getMapper().writer();
            if (formatted) {
                writer = writer.withDefaultPrettyPrinter();
            }
            return writer.writeValueAsString(obj);
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(String enc) {
        return deserialize(enc, Object.class);
    }

    @Override
    public Object deserialize(String text, Class<?> clazz) {
        try {
            Object obj = getMapper().readValue(text, clazz);
            return obj;
        } catch (Exception e) {
            throw new SerializeException(e.getMessage(), e);
        }
    }

    @Override
    public Object deserialize(String text, Object typeToken) {
        return deserialize(text, (TypeReference) typeToken);
    }

    public <T> T deserialize(String text, TypeReference<T> typeToken) {
        try {
            Object obj = getMapper().readValue(text, typeToken);
            return (T) obj;
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
