package i2f.spring.serialize.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

public class JacksonCsvSerializer extends AbsJacksonSerializer {
    private static CsvMapper mapper = new CsvMapper();

    @Override
    public ObjectMapper getMapper() {
        return mapper;
    }
}
