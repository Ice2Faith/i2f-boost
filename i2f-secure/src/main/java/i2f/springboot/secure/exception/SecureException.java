package i2f.springboot.secure.exception;


import i2f.springboot.secure.consts.SecureErrorCode;

/**
 * @author Ice2Faith
 * @date 2023/6/13 10:19
 * @desc
 */
public class SecureException extends RuntimeException {
    private SecureErrorCode code;
    public SecureException(SecureErrorCode code) {
        this.code=code;
    }

    public SecureException(SecureErrorCode code, String message) {
        super(message);
        this.code=code;
    }

    public SecureException(SecureErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code=code;
    }

    public SecureException(SecureErrorCode code, Throwable cause) {
        super(cause);
        this.code=code;
    }

    public SecureErrorCode code() {
        return code;
    }
}
