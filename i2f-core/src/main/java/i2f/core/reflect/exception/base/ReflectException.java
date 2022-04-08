package i2f.core.reflect.exception.base;

import i2f.core.annotations.remark.Author;
import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class ReflectException extends BoostException {
    public ReflectException() {
    }

    public ReflectException(String message) {
        super(message);
    }

    public ReflectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReflectException(Throwable cause) {
        super(cause);
    }
}
