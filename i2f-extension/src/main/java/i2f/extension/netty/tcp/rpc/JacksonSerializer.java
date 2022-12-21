package i2f.extension.netty.tcp.rpc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 提供JSON的序列化和反序列化
 * 在RPC的场景中，可能存在着泛型或者Object等不明确的类型
 * 因此单一的使用普通的JSON序列化方式，在反序列化之后
 * 在服务端或者客户端使用时，可能会存在一定的问题
 * 解决方案是借助Jackson的带类型序列化方式
 * 显示的指定序列化的每个值的类型
 * 在反序列化是拥有明确的类型，明确的进行反序列化
 */
public class JacksonSerializer {
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
