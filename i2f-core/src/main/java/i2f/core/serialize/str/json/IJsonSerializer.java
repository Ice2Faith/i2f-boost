package i2f.core.serialize.str.json;

import i2f.core.annotations.remark.Author;
import i2f.core.serialize.str.IStringObjectSerializer;
import i2f.core.serialize.str.json.exception.JsonSerializeException;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 17:26
 * @desc
 */
@Author("i2f")
public interface IJsonSerializer extends IStringObjectSerializer {
    default <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        String text = serialize(map);
        return (T) deserialize(text, clazz);
    }

    default Map<String, Object> bean2Map(Object obj) {
        throw new JsonSerializeException("json serializer not support bean2map");
    }
}
