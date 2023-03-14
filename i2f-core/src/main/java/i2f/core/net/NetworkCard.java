package i2f.core.net;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.NetworkInterface;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/3/14 9:42
 * @desc
 */
@Data
@NoArgsConstructor
public class NetworkCard {
    protected NetworkInterface networkInterface;
    protected NetworkInterface parent;
    protected String name;
    protected String displayName;
    protected int index;
    protected String mac;
    protected int mtu;
    protected boolean loopback;
    protected boolean up;
    protected boolean virtual;
    protected boolean multicast;
    protected boolean p2p;
    protected List<NetAddress> addresses;
}
