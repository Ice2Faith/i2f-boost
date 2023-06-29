package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

public class JacksonCsvSerializer extends AbsJacksonSerializer {
    private ObjectMapper mapper = new CsvMapper();

    public JacksonCsvSerializer() {
    }

    public JacksonCsvSerializer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public JacksonCsvSerializer(CsvMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
