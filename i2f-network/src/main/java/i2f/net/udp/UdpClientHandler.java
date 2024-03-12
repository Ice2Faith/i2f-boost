package i2f.net.udp;

import java.io.IOException;
import java.net.DatagramSocket;

/**
 * @author Ice2Faith
 * @date 2024/3/12 9:25
 * @desc
 */
public interface UdpClientHandler {
    default void onPrepared(DatagramSocket socket,UdpClient udpClient) throws IOException{

    }

    default void onClosed(UdpClient udpClient) throws IOException{

    }
}
