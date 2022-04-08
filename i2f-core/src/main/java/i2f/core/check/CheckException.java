package i2f.core.check;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/3/26 13:41
 * @desc
 */
public class CheckException extends BoostException {
    public CheckException() {
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }
}
