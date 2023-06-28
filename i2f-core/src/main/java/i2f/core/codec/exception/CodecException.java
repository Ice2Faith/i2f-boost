package i2f.core.codec.exception;

/**
 * @author Ice2Faith
 * @date 2023/6/19 16:01
 * @desc
 */
public class CodecException extends RuntimeException {
    public CodecException() {
    }

    public CodecException(String message) {
        super(message);
    }

    public CodecException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodecException(Throwable cause) {
        super(cause);
    }
}
