package i2f.core.cycle.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/4/27 11:19
 * @desc
 */
public class LifeCycleException extends BoostException {
    public LifeCycleException() {
    }

    public LifeCycleException(String message) {
        super(message);
    }

    public LifeCycleException(String message, Throwable cause) {
        super(message, cause);
    }

    public LifeCycleException(Throwable cause) {
        super(cause);
    }
}
