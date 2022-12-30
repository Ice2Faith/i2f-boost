package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;

public class JacksonProtobufSerializer extends AbsJacksonSerializer {
    private static ProtobufMapper mapper = new ProtobufMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
