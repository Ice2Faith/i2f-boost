package i2f.core.text.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/4/2 13:47
 * @desc
 */
public class TextFormatException extends BoostException {
    public TextFormatException() {
    }

    public TextFormatException(String message) {
        super(message);
    }

    public TextFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextFormatException(Throwable cause) {
        super(cause);
    }
}
