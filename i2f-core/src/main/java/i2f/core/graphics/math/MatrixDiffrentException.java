package i2f.core.graphics.math;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/6/20 11:16
 * @desc
 */
public class MatrixDiffrentException extends BoostException {
    public MatrixDiffrentException() {
    }

    public MatrixDiffrentException(String message) {
        super(message);
    }

    public MatrixDiffrentException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixDiffrentException(Throwable cause) {
        super(cause);
    }
}
