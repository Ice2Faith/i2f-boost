package i2f.core.serialize.json.exception;

import i2f.core.text.exception.TextFormatException;

/**
 * @author ltb
 * @date 2022/4/2 14:12
 * @desc
 */
public class JsonProcessException extends TextFormatException {
    public JsonProcessException() {
    }

    public JsonProcessException(String message) {
        super(message);
    }

    public JsonProcessException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonProcessException(Throwable cause) {
        super(cause);
    }
}
