package i2f.core.j2ee.firewall.exception;

import i2f.core.exception.BoostException;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:14
 * @desc
 */
public class FirewallException extends BoostException {
    public FirewallException() {
    }

    public FirewallException(String message) {
        super(message);
    }

    public FirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FirewallException(Throwable cause) {
        super(cause);
    }
}
