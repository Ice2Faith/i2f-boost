package i2f.core.j2ee.firewall.std.impl.path;

import i2f.core.j2ee.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:31
 * @desc
 */
public class PathFirewallException extends FirewallException {
    public PathFirewallException() {
    }

    public PathFirewallException(String message) {
        super(message);
    }

    public PathFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public PathFirewallException(Throwable cause) {
        super(cause);
    }
}
