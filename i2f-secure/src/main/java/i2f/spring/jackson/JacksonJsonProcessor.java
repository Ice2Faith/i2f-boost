package i2f.spring.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JacksonJsonProcessor {
    protected volatile ObjectMapper mapper;


    public JacksonJsonProcessor(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public synchronized JacksonJsonProcessor setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
        return this;
    }

    public String serialize(Object obj) {
        try {
            return getMapper().writeValueAsString(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String serialize(Object obj, boolean formatted) {
        try {
            if (formatted) {
                return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            }
            return getMapper().writeValueAsString(obj);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(String text, Class<T> clazz) {
        try {
            return getMapper().readValue(text, clazz);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public <T> T deserialize(String text, Object typeToken) {
        try {
            TypeReference<T> typeRef = (TypeReference<T>) typeToken;
            return getMapper().readValue(text, typeRef);
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Map<String, Object> bean2Map(Object bean) {
        String json = serialize(bean);
        Map<String, Object> map = deserialize(json, new TypeReference<Map<String, Object>>() {
        });
        return map;
    }

    public <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        String json = serialize(map);
        T bean = deserialize(json, clazz);
        return bean;
    }
}
