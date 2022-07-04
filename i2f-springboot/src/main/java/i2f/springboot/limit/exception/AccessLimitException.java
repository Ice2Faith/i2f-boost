package i2f.springboot.limit.exception;

/**
 * @author ltb
 * @date 2022/7/2 22:27
 * @desc
 */
public class AccessLimitException extends RuntimeException{
    public AccessLimitException() {
    }

    public AccessLimitException(String message) {
        super(message);
    }

    public AccessLimitException(String message, Throwable cause) {
        super(message, cause);
    }
}
