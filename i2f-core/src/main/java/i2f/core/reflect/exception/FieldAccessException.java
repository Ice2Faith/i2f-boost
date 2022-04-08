package i2f.core.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class FieldAccessException extends ReflectException {
    public FieldAccessException() {
    }

    public FieldAccessException(String message) {
        super(message);
    }

    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldAccessException(Throwable cause) {
        super(cause);
    }
}
