package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;

public class JacksonCborSerializer extends AbsJacksonSerializer {
    private static CBORMapper mapper = new CBORMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
