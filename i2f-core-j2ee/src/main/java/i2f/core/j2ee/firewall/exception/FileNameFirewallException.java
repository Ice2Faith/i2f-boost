package i2f.core.j2ee.firewall.exception;

/**
 * @author Ice2Faith
 * @date 2023/8/28 10:34
 * @desc 敏感文件攻击异常
 */
public class FileNameFirewallException extends FirewallException {
    public FileNameFirewallException() {
    }

    public FileNameFirewallException(String message) {
        super(message);
    }

    public FileNameFirewallException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileNameFirewallException(Throwable cause) {
        super(cause);
    }
}
