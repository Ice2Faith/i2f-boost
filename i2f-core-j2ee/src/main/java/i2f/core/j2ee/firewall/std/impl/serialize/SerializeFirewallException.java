package i2f.core.j2ee.firewall.std.impl.serialize;

import i2f.core.j2ee.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:31
 * @desc
 */
public class SerializeFirewallException extends FirewallException {
    public SerializeFirewallException() {
    }

    public SerializeFirewallException(String message) {
        super(message);
    }

    public SerializeFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializeFirewallException(Throwable cause) {
        super(cause);
    }
}
