package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JacksonYamlSerializer extends AbsJacksonSerializer {
    private YAMLMapper mapper = new YAMLMapper();

    public JacksonYamlSerializer() {
    }

    public JacksonYamlSerializer(YAMLMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
