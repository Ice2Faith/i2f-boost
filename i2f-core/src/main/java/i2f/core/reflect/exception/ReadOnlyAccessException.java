package i2f.core.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class ReadOnlyAccessException extends ReflectException {
    public ReadOnlyAccessException() {
    }

    public ReadOnlyAccessException(String message) {
        super(message);
    }

    public ReadOnlyAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReadOnlyAccessException(Throwable cause) {
        super(cause);
    }
}
