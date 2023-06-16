package i2f.springboot.secure.customizer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public abstract class JacksonDatetimeFormatterDeserializer<T extends Temporal> extends JsonDeserializer<T> {
    private DateTimeFormatter formatter;

    public JacksonDatetimeFormatterDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String str = jsonParser.getText();
        if (str == null) {
            return null;
        }
        return parse(str, formatter);
    }

    public abstract T parse(String str, DateTimeFormatter formatter) throws JsonProcessingException;
}
