package i2f.core.reflection.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class FieldNotFoundException extends ReflectException {
    public FieldNotFoundException() {
    }

    public FieldNotFoundException(String message) {
        super(message);
    }

    public FieldNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public FieldNotFoundException(Throwable cause) {
        super(cause);
    }
}
