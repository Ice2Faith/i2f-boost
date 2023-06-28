package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import i2f.core.serialize.str.json.IJsonSerializer;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

public class JacksonJsonSerializer extends AbsJacksonSerializer implements IJsonSerializer {
    public static String DEFAULT_DATE_FMT = "yyyy-MM-dd HH:mm:ss SSS";
    public static JacksonJsonSerializer INSTANCE = new Supplier<JacksonJsonSerializer>() {
        @Override
        public JacksonJsonSerializer get() {
            JsonMapper mapper = new JsonMapper();
            mapper.setLocale(Locale.getDefault());
            mapper.setDateFormat(new SimpleDateFormat(DEFAULT_DATE_FMT));
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            return new JacksonJsonSerializer(mapper);
        }
    }.get();

    public JacksonJsonSerializer() {
    }

    public JacksonJsonSerializer(JsonMapper mapper) {
        this.mapper = mapper;
    }

    private JsonMapper mapper = new JsonMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return deserialize(json, new TypeReference<Map<String, Object>>() {
        });
    }
}
