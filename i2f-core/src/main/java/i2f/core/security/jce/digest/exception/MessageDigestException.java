package i2f.core.security.jce.digest.exception;

/**
 * @author Ice2Faith
 * @date 2023/6/19 16:15
 * @desc
 */
public class MessageDigestException extends RuntimeException {
    public MessageDigestException() {
    }

    public MessageDigestException(String message) {
        super(message);
    }

    public MessageDigestException(String message, Throwable cause) {
        super(message, cause);
    }

    public MessageDigestException(Throwable cause) {
        super(cause);
    }
}
