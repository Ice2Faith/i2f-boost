package i2f.springboot.secure.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/13 10:19
 * @desc
 */
public enum SecureErrorCode {
    SECURE_HEADER_EMPTY(101),
    SECURE_HEADER_STRUCTURE(102),
    SECURE_HEADER_SIGN_EMPTY(103),
    SECURE_HEADER_NONCE_EMPTY(104),
    SECURE_HEADER_RANDOM_KEY_EMPTY(105),
    SECURE_HEADER_ASYM_SIGN_EMPTY(106),

    BAD_SIGN(201),
    BAD_RANDOM_KEY(202),
    BAD_NONCE(203),

    BAD_SECURE_REQUEST(301);

    private int code;

    SecureErrorCode(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }
}
