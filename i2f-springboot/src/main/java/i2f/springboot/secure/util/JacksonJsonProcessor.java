package i2f.springboot.secure.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 15:52
 * @desc
 */
@Component
public class JacksonJsonProcessor  {
    protected volatile ObjectMapper mapper;
    public JacksonJsonProcessor(){
        mapper=new ObjectMapper();
        mapper.setLocale(Locale.getDefault());
    }
    public JacksonJsonProcessor(ObjectMapper mapper){
        this.mapper=mapper;
    }
    public ObjectMapper getMapper(){
        return mapper;
    }

    public String toText(Object obj)  {
        try{
            return getMapper().writeValueAsString(obj);
        }catch(Throwable e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public <T> T parseText(String text, Class<T> clazz) {
        try{
            return getMapper().readValue(text,clazz);
        }catch(Throwable e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public <T> T parseTextRef(String text, Object typeToken) {
        try{
            TypeReference<T> typeRef=(TypeReference<T>)typeToken;
            return getMapper().readValue(text,typeRef);
        }catch(Throwable e){
            throw new RuntimeException(e.getMessage(),e);
        }
    }

    public Map<String,Object> bean2Map(Object bean) {
        String json=toText(bean);
        Map<String, Object> map=parseTextRef(json, new TypeReference<Map<String, Object>>() {});
        return map;
    }

    public<T> T map2Bean(Map<String,Object> map, Class<T> clazz) {
        String json=toText(map);
        T bean=parseText(json,clazz);
        return bean;
    }
}
