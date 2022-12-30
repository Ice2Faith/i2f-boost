package i2f.core.text.exception;


import i2f.core.serialize.SerializeException;

/**
 * @author ltb
 * @date 2022/4/14 10:18
 * @desc
 */
public class TextSerializeException extends SerializeException {
    public TextSerializeException() {
    }

    public TextSerializeException(String message) {
        super(message);
    }

    public TextSerializeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TextSerializeException(Throwable cause) {
        super(cause);
    }
}
