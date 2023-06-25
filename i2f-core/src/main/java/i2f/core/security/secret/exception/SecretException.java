package i2f.core.security.secret.exception;

/**
 * @author ltb
 * @date 2022/10/19 15:51
 * @desc
 */
public class SecretException extends RuntimeException {
    public SecretException() {
    }

    public SecretException(String message) {
        super(message);
    }

    public SecretException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecretException(Throwable cause) {
        super(cause);
    }
}
