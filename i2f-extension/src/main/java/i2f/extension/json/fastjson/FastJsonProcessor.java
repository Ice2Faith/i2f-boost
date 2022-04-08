package i2f.extension.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import i2f.core.json.impl.AbstractJsonProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 21:29
 * @desc
 */
public class FastJsonProcessor extends AbstractJsonProcessor {

    @Override
    public String toText(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T parseText(String text, Class<T> clazz) {
        return JSON.parseObject(text,clazz);
    }

    @Override
    public <T> T parseTextRef(String text, Object typeToken) {
        TypeReference<T> typeRef=(TypeReference<T>)typeToken;
        return JSON.parseObject(text,typeRef);
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json=toText(obj);
        return parseTextRef(json,new TypeReference<Map<String,Object>>(){});
    }
}
