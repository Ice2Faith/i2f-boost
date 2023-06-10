package i2f.liteflow.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import i2f.liteflow.consts.LiteFlowErrorCode;
import i2f.liteflow.exception.LiteFlowException;

import java.util.Locale;
import java.util.TimeZone;

/**
 * @author Ice2Faith
 * @date 2023/6/7 15:09
 * @desc
 */
public class Json {
    private static ObjectMapper mapper=new ObjectMapper();
    static {
        mapper.setLocale(Locale.CHINA);
        mapper.setTimeZone(TimeZone.getDefault());
    }
    public static String toJson(Object obj,boolean pretty){
        try{
            if(pretty){
                return mapper.writerWithDefaultPrettyPrinter()
                        .writeValueAsString(obj);
            }
            return mapper.writeValueAsString(obj);
        }catch(Exception e){
            throw new LiteFlowException(LiteFlowErrorCode.JSON_STRINGIFY,"转换为JSON失败",e);
        }
    }

    public static<T> T parseJson(String json,Class<T> clazz){
        try{
            return mapper.readValue(json,clazz);
        }catch(Exception e){
            throw new LiteFlowException(LiteFlowErrorCode.JSON_PARSE,"解析JSON失败",e);
        }
    }

    public static<T> T parseJson(String json, TypeReference<T> typeRef){
        try{
            return mapper.readValue(json,typeRef);
        }catch(Exception e){
            throw new LiteFlowException(LiteFlowErrorCode.JSON_PARSE,"解析JSON失败",e);
        }
    }
}
