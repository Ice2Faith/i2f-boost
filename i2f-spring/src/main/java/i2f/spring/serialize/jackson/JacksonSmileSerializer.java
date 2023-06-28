package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

public class JacksonSmileSerializer extends AbsJacksonSerializer {
    private SmileMapper mapper = new SmileMapper();

    public JacksonSmileSerializer() {
    }

    public JacksonSmileSerializer(SmileMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
