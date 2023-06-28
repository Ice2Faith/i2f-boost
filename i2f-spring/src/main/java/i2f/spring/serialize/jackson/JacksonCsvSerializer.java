package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

public class JacksonCsvSerializer extends AbsJacksonSerializer {
    private CsvMapper mapper = new CsvMapper();

    public JacksonCsvSerializer() {
    }

    public JacksonCsvSerializer(CsvMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
