package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

public class JacksonSmileSerializer extends AbsJacksonSerializer {
    private static SmileMapper mapper = new SmileMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
