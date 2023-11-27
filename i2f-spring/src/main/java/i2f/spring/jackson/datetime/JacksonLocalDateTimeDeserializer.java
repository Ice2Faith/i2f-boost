package i2f.spring.jackson.datetime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import i2f.core.type.date.DateFormatter;
import i2f.spring.jackson.base.BaseJacksonContextDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonLocalDateTimeDeserializer extends BaseJacksonContextDeserializer<LocalDateTime> {
    private DateTimeFormatter formatter;

    public JacksonLocalDateTimeDeserializer() {
    }

    public JacksonLocalDateTimeDeserializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String str = jsonParser.getText();
        if (str == null) {
            return null;
        }

        try{
            return parse(str, formatter);
        }catch(Exception e){
            if(e instanceof JsonProcessingException){
                throw (JsonProcessingException)e;
            }else {
                throw new IOException(e.getMessage(), e);
            }
        }

    }

    public LocalDateTime parse(String str, DateTimeFormatter formatter) throws Exception{
        try{
            if(formatter!=null){
                return LocalDateTime.parse(str,formatter);
            }
        }catch(Exception e){

        }
        try{
            if(formatPatten!=null){
                return DateFormatter.parseLocalDateTime(formatPatten,str);
            }
        }catch(Exception e1){

        }
        return DateFormatter.parseLocalDateTime(str);
    }

}
