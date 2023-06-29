package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;

public class JacksonCborSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new CBORMapper();

    public JacksonCborSerializer() {
    }

    public JacksonCborSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonCborSerializer(CBORMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
