package i2f.extension.json.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import i2f.core.serialize.json.impl.AbstractJsonProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/26 21:29
 * @desc
 */
public class FastJsonProcessor extends AbstractJsonProcessor {


    @Override
    public String serialize(Object obj) {
        return JSON.toJSONString(obj);
    }

    @Override
    public <T> T deserialize(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }

    @Override
    public <T> T deserialize(String text, Object typeToken) {
        TypeReference<T> typeRef = (TypeReference<T>) typeToken;
        return JSON.parseObject(text, typeRef);
    }

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        String json = serialize(obj);
        return deserialize(json, new TypeReference<Map<String, Object>>() {
        });
    }
}
