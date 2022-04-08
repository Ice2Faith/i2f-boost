package i2f.core.json;

import i2f.core.annotations.remark.Author;
import i2f.core.text.IFormatTextProcessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/24 17:26
 * @desc
 */
@Author("i2f")
public interface IJsonProcessor extends IFormatTextProcessor {
    <T> T map2Bean(Map<String,Object> map,Class<T> clazz);
    Map<String,Object> bean2Map(Object obj);
}
