package i2f.core.j2ee.firewall.exception;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc URL注入攻击异常
 */
public class UrlInjectFirewallException extends FirewallException {
    public UrlInjectFirewallException() {
    }

    public UrlInjectFirewallException(String message) {
        super(message);
    }

    public UrlInjectFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public UrlInjectFirewallException(Throwable cause) {
        super(cause);
    }
}
