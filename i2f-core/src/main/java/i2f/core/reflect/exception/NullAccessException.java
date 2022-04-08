package i2f.core.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class NullAccessException extends ReflectException {
    public NullAccessException() {
    }

    public NullAccessException(String message) {
        super(message);
    }

    public NullAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public NullAccessException(Throwable cause) {
        super(cause);
    }
}
