package i2f.liteflow.exception;

import i2f.liteflow.consts.LiteFlowErrorCode;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:16
 * @desc
 */
public class LiteFlowException extends RuntimeException {
    private LiteFlowErrorCode code;

    public LiteFlowException(LiteFlowErrorCode code) {
        this.code = code;
    }

    public LiteFlowException(LiteFlowErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public LiteFlowException(LiteFlowErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public LiteFlowException(LiteFlowErrorCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public LiteFlowErrorCode getCode() {
        return this.code;
    }
}
