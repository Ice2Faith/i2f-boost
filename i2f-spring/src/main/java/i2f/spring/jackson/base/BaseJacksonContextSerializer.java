package i2f.spring.jackson.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

/**
 * @author Ice2Faith
 * @date 2023/11/27 9:49
 * @desc
 */
public abstract class BaseJacksonContextSerializer<T> extends JsonSerializer<T> implements ContextualSerializer {
    protected BeanProperty beanProperty;
    protected Class<?> fieldClass;
    protected JsonFormat fieldFormat;
    protected String formatPatten;

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        this.beanProperty=beanProperty;
        this.fieldClass = beanProperty.getType().getRawClass();
        this.fieldFormat = this.fieldClass.getAnnotation(JsonFormat.class);
        if(this.fieldFormat==null){
            this.fieldFormat = this.fieldClass.getDeclaredAnnotation(JsonFormat.class);
        }
        if(this.fieldFormat!=null){
            this.formatPatten=this.fieldFormat.pattern();
        }
        return this;
    }

}
