package i2f.core.serialize.json.impl;

import i2f.core.serialize.json.IJsonProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/4/2 13:56
 * @desc
 */
public abstract class AbstractJsonProcessor implements IJsonProcessor {

    @Override
    public <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        String json = serialize(map);
        return deserialize(json, clazz);
    }

}
