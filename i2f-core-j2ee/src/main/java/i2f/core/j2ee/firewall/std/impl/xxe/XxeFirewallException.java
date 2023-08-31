package i2f.core.j2ee.firewall.std.impl.xxe;

import i2f.core.j2ee.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:30
 * @desc
 */
public class XxeFirewallException extends FirewallException {
    public XxeFirewallException() {
    }

    public XxeFirewallException(String message) {
        super(message);
    }

    public XxeFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public XxeFirewallException(Throwable cause) {
        super(cause);
    }
}
