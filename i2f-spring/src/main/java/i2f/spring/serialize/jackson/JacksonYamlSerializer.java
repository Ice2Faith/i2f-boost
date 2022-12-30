package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

public class JacksonYamlSerializer extends AbsJacksonSerializer {
    private static YAMLMapper mapper = new YAMLMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
