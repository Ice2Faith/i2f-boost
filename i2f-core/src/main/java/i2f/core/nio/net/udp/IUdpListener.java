package i2f.core.nio.net.udp;

import java.io.IOException;
import java.nio.channels.DatagramChannel;

/**
 * @author ltb
 * @date 2022/5/11 9:50
 * @desc
 */
public interface IUdpListener {

    void onRead(DatagramChannel sc, IUdpConnector server) throws IOException;

    void onWrite(DatagramChannel sc,IUdpConnector server) throws IOException;
}
