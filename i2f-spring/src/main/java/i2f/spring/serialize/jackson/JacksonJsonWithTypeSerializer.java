package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

public class JacksonJsonWithTypeSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new JsonMapper();

    public JacksonJsonWithTypeSerializer() {
    }

    public JacksonJsonWithTypeSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonJsonWithTypeSerializer(JsonMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.WRAPPER_ARRAY);
        return mapper;
    }
}
