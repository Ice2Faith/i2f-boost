package i2f.spring.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * @author ltb
 * @date 2021/8/10
 */
@Component
public class JacksonUtil {
    private static String PROJ_DEFAULT_DATE_FMT="yyyy-MM-dd HH:mm:ss SSS";
    private static volatile JacksonJsonProcessor mapper;
    private JacksonUtil(){

    }

    public synchronized static void setMapper(JacksonJsonProcessor mapper) {
        JacksonUtil.mapper = mapper;
    }

    public static JacksonJsonProcessor getMapper(){
        if(mapper==null){
            synchronized (JacksonUtil.class){
                if(mapper==null){
                    mapper=new JacksonJsonProcessor();
                }
            }
        }
        mapper.getMapper().setDateFormat(new SimpleDateFormat(PROJ_DEFAULT_DATE_FMT));
        mapper.getMapper().setSerializationInclusion(JsonInclude.Include.ALWAYS);
        return mapper;
    }
    public static String toJson(Object obj)  {
        return getMapper().toText(obj);
    }
    public static<T> T parseObj(String json,Class<T> clazz)   {
        return getMapper().parseText(json,clazz);
    }
    public static<T> T parseRef(String json, TypeReference<T> ref)   {
        return getMapper().parseTextRef(json,ref);
    }
    public static Map<String,Object> bean2Map(Object bean)   {
        return getMapper().bean2Map(bean);
    }
    public static<T> T map2Bean(Map<String,Object> map,Class<T> clazz)   {
        return getMapper().map2Bean(map,clazz);
    }
}
