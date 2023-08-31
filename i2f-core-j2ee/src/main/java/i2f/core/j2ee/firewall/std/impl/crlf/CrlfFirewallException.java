package i2f.core.j2ee.firewall.std.impl.crlf;

import i2f.core.j2ee.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:30
 * @desc
 */
public class CrlfFirewallException extends FirewallException {
    public CrlfFirewallException() {
    }

    public CrlfFirewallException(String message) {
        super(message);
    }

    public CrlfFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public CrlfFirewallException(Throwable cause) {
        super(cause);
    }
}
