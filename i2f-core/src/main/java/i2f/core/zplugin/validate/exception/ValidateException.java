package i2f.core.zplugin.validate.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/3/28 9:24
 * @desc
 */
public class ValidateException extends BoostException {
    public ValidateException() {
        super();
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }
}
