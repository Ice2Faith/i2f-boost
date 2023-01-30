package i2f.springboot.sensible;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import i2f.springboot.sensible.handler.TruncateSensibleHandler;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class JacksonSensibleSerializer extends JsonSerializer<Object>
        implements ContextualSerializer, ApplicationContextAware {

    private ISensibleHandler handler;
    private Sensible ann;

    @Override
    public void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        obj = handler.handle(obj, ann);
        jsonGenerator.writeObject(obj);
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty == null) {
            return serializerProvider.findNullValueSerializer(null);
        }
        Sensible ann = beanProperty.getAnnotation(Sensible.class);
        if (ann == null) {
            ann = beanProperty.getContextAnnotation(Sensible.class);
        }
        if (ann == null) {
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        Class<?> rawClass = beanProperty.getType().getRawClass();
        Map<String, ISensibleHandler> beans = getBeans(ISensibleHandler.class);
        for (Map.Entry<String, ISensibleHandler> entry : beans.entrySet()) {
            ISensibleHandler handler = entry.getValue();
            Set<String> type = handler.accept();
            if (type.contains(ann.type())) {
                Set<Class<?>> types = handler.type();
                for (Class<?> tp : types) {
                    if (tp.isAssignableFrom(rawClass)) {
                        JacksonSensibleSerializer serializer = new JacksonSensibleSerializer();
                        serializer.handler = handler;
                        serializer.ann = ann;
                        return serializer;
                    }
                }
            }
        }
        JacksonSensibleSerializer serializer = new JacksonSensibleSerializer();
        serializer.handler = new TruncateSensibleHandler();
        serializer.ann = ann;
        return serializer;
    }

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        Map<String, T> ret = new HashMap<>();
        String[] names = context.getBeanNamesForType(clazz);
        for (String name : names) {
            Object bean = context.getBean(name);
            ret.put(name, (T) bean);
        }
        return ret;
    }
}
