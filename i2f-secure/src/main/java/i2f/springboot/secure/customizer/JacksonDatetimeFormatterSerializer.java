package i2f.springboot.secure.customizer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonDatetimeFormatterSerializer<T extends Temporal> extends JsonSerializer<T> {
    private DateTimeFormatter formatter;

    public JacksonDatetimeFormatterSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void serialize(T temporal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (temporal != null) {
            jsonGenerator.writeString(formatter.format(temporal));
        } else {
            serializerProvider.defaultSerializeNull(jsonGenerator);
        }
    }
}
