package i2f.core.json;

import i2f.core.annotations.remark.Author;
import i2f.core.json.impl.AbstractJsonProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 17:29
 * @desc
 */
@Author("i2f")
public class Json2Processor extends AbstractJsonProcessor {

    @Override
    public Map<String, Object> bean2Map(Object obj) {
        throw new UnsupportedOperationException("Json2 un-support bean2Map.");
    }

    @Override
    public String toText(Object obj) {
        return Json2.toJson(obj);
    }

    @Override
    public <T> T parseText(String json, Class<T> clazz) {
        throw new UnsupportedOperationException("Json2 un-support parseText.");
    }

    @Override
    public <T> T parseTextRef(String json, Object typeToken) {
        throw new UnsupportedOperationException("Json2 un-support parseTextRef.");
    }
}
