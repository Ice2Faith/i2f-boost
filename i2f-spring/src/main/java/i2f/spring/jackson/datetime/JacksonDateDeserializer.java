package i2f.spring.jackson.datetime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import i2f.core.type.date.DateFormatter;
import i2f.spring.jackson.base.BaseJacksonContextDeserializer;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonDateDeserializer extends BaseJacksonContextDeserializer<Date> {
    private DateFormat formatter;

    public JacksonDateDeserializer() {
    }

    public JacksonDateDeserializer(DateFormat formatter) {
        this.formatter = formatter;
    }

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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

    public Date parse(String str, DateFormat formatter) throws Exception{
        try{
            if(formatter!=null){
                return formatter.parse(str);
            }
        }catch(Exception e){

        }
        try{
            if(formatPatten!=null){
                return DateFormatter.parse(formatPatten,str);
            }
        }catch(Exception e1){

        }
        return DateFormatter.parse(str);
    }

}
