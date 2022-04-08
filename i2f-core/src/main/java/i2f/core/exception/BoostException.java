package i2f.core.exception;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2022/3/19 15:02
 * @desc
 */
@Author("i2f")
public class BoostException extends RuntimeException{
    public BoostException() {
    }

    public BoostException(String message) {
        super(message);
    }

    public BoostException(String message, Throwable cause) {
        super(message, cause);
    }

    public BoostException(Throwable cause) {
        super(cause);
    }
}
