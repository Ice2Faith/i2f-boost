package i2f.core.reflection.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/11 14:15
 * @desc
 */
@Author("i2f")
public class MethodNotFoundException extends ReflectException {
    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
    }
}
