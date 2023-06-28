package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;

public class JacksonProtobufSerializer extends AbsJacksonSerializer {
    private ProtobufMapper mapper = new ProtobufMapper();

    public JacksonProtobufSerializer() {
    }

    public JacksonProtobufSerializer(ProtobufMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
