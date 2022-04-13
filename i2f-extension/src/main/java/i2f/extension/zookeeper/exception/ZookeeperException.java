package i2f.extension.zookeeper.exception;

import i2f.core.exception.BoostException;

/**
 * @author ltb
 * @date 2022/4/13 9:07
 * @desc
 */
public class ZookeeperException extends BoostException {
    public ZookeeperException() {
    }

    public ZookeeperException(String message) {
        super(message);
    }

    public ZookeeperException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZookeeperException(Throwable cause) {
        super(cause);
    }
}
