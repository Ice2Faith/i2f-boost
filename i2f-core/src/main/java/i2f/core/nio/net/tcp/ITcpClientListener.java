package i2f.core.nio.net.tcp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author ltb
 * @date 2022/5/11 9:59
 * @desc
 */
public interface ITcpClientListener extends ITcpListener{
    void onConnect(SocketChannel sc,TcpClient client) throws IOException;
}
