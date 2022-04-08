package i2f.core.text;

import i2f.core.annotations.notice.CloudExcept;
import i2f.core.annotations.remark.Author;
import i2f.core.text.exception.TextFormatException;

/**
 * @author ltb
 * @date 2022/3/24 17:45
 * @desc
 */
@Author("i2f")
@CloudExcept(TextFormatException.class)
public interface IFormatTextProcessor {
    String toText(Object obj);
    <T> T parseText(String text,Class<T> clazz);
    <T> T parseTextRef(String text,Object typeToken);
}
