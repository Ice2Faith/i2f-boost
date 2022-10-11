package i2f.spring.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import i2f.core.text.IFormatTextProcessor;
import i2f.core.text.exception.TextFormatException;

/**
 * @author ltb
 * @date 2022/10/11 15:14
 * @desc
 */
public class JacksonTextSerializer implements IFormatTextProcessor {
    @Override
    public String toText(Object obj) {
        try {
            return JacksonSerializer.toJson(obj);
        } catch (Throwable e) {
            throw new TextFormatException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T parseText(String text, Class<T> clazz) {
        try {
            return JacksonSerializer.parseJson(text, clazz);
        } catch (Throwable e) {
            throw new TextFormatException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T parseTextRef(String text, Object typeToken) {
        try {
            return JacksonSerializer.parseJson(text, (TypeReference<T>) typeToken);
        } catch (Throwable e) {
            throw new TextFormatException(e.getMessage(), e);
        }
    }
}
