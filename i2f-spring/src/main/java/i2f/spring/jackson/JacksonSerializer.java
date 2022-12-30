package i2f.spring.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import i2f.spring.serialize.jackson.AbsJacksonSerializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * @author ltb
 * @date 2022/10/4 15:22
 * @desc
 */
public class JacksonSerializer extends AbsJacksonSerializer {
    private static ObjectMapper mapper = null;

    public static ObjectMapper serializer() {
        if (mapper == null) {
            synchronized (JacksonSerializer.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper();
                    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS"));
                    mapper.setLocale(Locale.CHINA);
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.setTimeZone(TimeZone.getTimeZone("+08"));
                    mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
                }
            }
        }
        return mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return serializer();
    }

    public static String toJson(Object obj) throws Exception {
        return serializer().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
    }

    public static <T> T parseJson(String json, Class<T> clazz) throws Exception {
        return serializer().readValue(json, clazz);
    }

    public static <T> T parseJson(String json, TypeReference<T> ref) throws Exception {
        return serializer().readValue(json, ref);
    }

}
