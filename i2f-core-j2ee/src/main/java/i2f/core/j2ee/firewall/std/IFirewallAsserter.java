package i2f.core.j2ee.firewall.std;

/**
 * @author Ice2Faith
 * @date 2023/8/31 16:43
 * @desc
 */
public interface IFirewallAsserter<T> {
    void doAssert(String errorMsg, T value);
}
