package i2f.core.serialize.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/4/14 11:15
 * @desc
 */
public class SerializeException extends BoostException {
    public SerializeException() {
    }

    public SerializeException(String message) {
        super(message);
    }

    public SerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeException(Throwable cause) {
        super(cause);
    }
}
