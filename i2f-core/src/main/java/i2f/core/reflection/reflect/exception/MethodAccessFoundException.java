package i2f.core.reflection.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class MethodAccessFoundException extends ReflectException {
    public MethodAccessFoundException() {
    }

    public MethodAccessFoundException(String message) {
        super(message);
    }

    public MethodAccessFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodAccessFoundException(Throwable cause) {
        super(cause);
    }
}
