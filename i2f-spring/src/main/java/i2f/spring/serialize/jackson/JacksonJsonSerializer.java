package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class JacksonJsonSerializer extends AbsJacksonSerializer {
    private static JsonMapper mapper = new JsonMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
