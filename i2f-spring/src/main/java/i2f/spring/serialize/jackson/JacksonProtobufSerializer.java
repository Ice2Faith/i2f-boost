package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.protobuf.ProtobufMapper;

public class JacksonProtobufSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new ProtobufMapper();

    public JacksonProtobufSerializer() {
    }

    public JacksonProtobufSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonProtobufSerializer(ProtobufMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
