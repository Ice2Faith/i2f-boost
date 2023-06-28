package i2f.core.serialize.str.json.exception;

import i2f.core.serialize.exception.SerializeException;

/**
 * @author ltb
 * @date 2022/4/2 14:12
 * @desc
 */
public class JsonSerializeException extends SerializeException {
    public JsonSerializeException() {
    }

    public JsonSerializeException(String message) {
        super(message);
    }

    public JsonSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonSerializeException(Throwable cause) {
        super(cause);
    }
}
