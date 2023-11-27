package i2f.spring.jackson.datetime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import i2f.core.type.date.DateFormatter;
import i2f.spring.jackson.base.BaseJacksonContextSerializer;

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
public class JacksonTemporalSerializer<T extends Temporal> extends BaseJacksonContextSerializer<T> {
    private DateTimeFormatter formatter;

    public JacksonTemporalSerializer() {
    }

    public JacksonTemporalSerializer(DateTimeFormatter formatter) {
        this.formatter = formatter;
    }

    @Override
    public void serialize(T temporal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (temporal != null) {
            try{
                String str=format(temporal,formatter);
                if(str!=null) {
                    jsonGenerator.writeString(str);
                }else{
                    serializerProvider.defaultSerializeValue(temporal,jsonGenerator);
                }
            }catch(Exception e){
                if(e instanceof IOException){
                    throw (IOException)e;
                }else{
                    throw new IOException(e.getMessage(),e);
                }
            }
        } else {
            serializerProvider.defaultSerializeNull(jsonGenerator);
        }
    }

    public String format(T temporal,DateTimeFormatter formatter) throws Exception{
        try{
            if(formatter!=null){
                return formatter.format(temporal);
            }
        }catch(Exception e){

        }
        if(formatPatten!=null){
            return DateTimeFormatter.ofPattern(formatPatten).format(temporal);
        }
        return null;
    }
}
