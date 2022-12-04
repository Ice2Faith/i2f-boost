package i2f.core.safe;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/3/26 13:41
 * @desc
 */
public class AssertException extends BoostException {
    public AssertException() {
    }

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssertException(Throwable cause) {
        super(cause);
    }
}
