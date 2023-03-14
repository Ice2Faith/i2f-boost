package i2f.core.net;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetAddress;

/**
 * @author Ice2Faith
 * @date 2023/3/14 10:01
 * @desc
 */
@Data
@NoArgsConstructor
public class NetAddress {
    protected InetAddress inetAddress;
    protected boolean ipv4;
    protected String addr;
    protected boolean loopback;
    protected boolean multicast;
    protected boolean reachable;
}
