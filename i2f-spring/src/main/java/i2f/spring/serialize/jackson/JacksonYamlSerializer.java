package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JacksonYamlSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new YAMLMapper();

    public JacksonYamlSerializer() {
    }

    public JacksonYamlSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonYamlSerializer(YAMLMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
