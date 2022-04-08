package i2f.core.reflect.exception;


import i2f.core.annotations.remark.Author;
import i2f.core.reflect.exception.base.ReflectException;

/**
 * @author ltb
 * @date 2022/3/17 10:54
 * @desc
 */
@Author("i2f")
public class InstanceException extends ReflectException {
    public InstanceException() {
    }

    public InstanceException(String message) {
        super(message);
    }

    public InstanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public InstanceException(Throwable cause) {
        super(cause);
    }
}
