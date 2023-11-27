package i2f.spring.jackson.datetime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import i2f.core.type.date.DateFormatter;
import i2f.spring.jackson.base.BaseJacksonContextSerializer;

import java.io.IOException;
import java.text.DateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/6/16 23:18
 * @desc
 */
public class JacksonDateSerializer<T extends Date> extends BaseJacksonContextSerializer<T> {
    private DateFormat formatter;

    public JacksonDateSerializer() {
    }

    public JacksonDateSerializer(DateFormat formatter) {
        this.formatter = formatter;
    }

    @Override
    public void serialize(T date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (date != null) {
            try{
                String str=format(date,formatter);
                if(str!=null) {
                    jsonGenerator.writeString(str);
                }else{
                    serializerProvider.defaultSerializeValue(date,jsonGenerator);
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

    public String format(T date,DateFormat formatter) throws Exception{
        try{
            if(formatter!=null){
                return formatter.format(date);
            }
        }catch(Exception e){

        }
        if(formatPatten!=null){
            return DateFormatter.format(formatPatten,date);
        }
        return null;
    }
}
