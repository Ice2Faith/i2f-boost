package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class JacksonXmlSerializer extends AbsJacksonSerializer {
    private static XmlMapper mapper = new XmlMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
