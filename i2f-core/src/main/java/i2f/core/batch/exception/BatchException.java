package i2f.core.batch.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/4/14 14:36
 * @desc
 */
public class BatchException extends BoostException {
    public BatchException() {
    }

    public BatchException(String message) {
        super(message);
    }

    public BatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public BatchException(Throwable cause) {
        super(cause);
    }
}
