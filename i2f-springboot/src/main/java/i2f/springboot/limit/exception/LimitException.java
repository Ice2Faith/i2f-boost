package i2f.springboot.limit.exception;

import i2f.springboot.limit.consts.LimitType;

/**
 * @author Ice2Faith
 * @date 2023/8/3 10:07
 * @desc
 */
public class LimitException extends RuntimeException {
    private LimitType type;
    public LimitException(LimitType type) {
        this.type=type;
    }

    public LimitException(LimitType type,String message) {
        super(message);
        this.type=type;
    }

    public LimitException(LimitType type,String message, Throwable cause) {
        super(message, cause);
        this.type=type;
    }

    public LimitException(LimitType type,Throwable cause) {
        super(cause);
        this.type=type;
    }

    public LimitType type(){
        return this.type;
    }
}
