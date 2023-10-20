package i2f.core.j2ee.firewall.std.impl.xss;


import i2f.core.j2ee.firewall.exception.FirewallException;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:31
 * @desc
 */
public class XssFirewallException extends FirewallException {
    public XssFirewallException() {
    }

    public XssFirewallException(String message) {
        super(message);
    }

    public XssFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public XssFirewallException(Throwable cause) {
        super(cause);
    }
}
