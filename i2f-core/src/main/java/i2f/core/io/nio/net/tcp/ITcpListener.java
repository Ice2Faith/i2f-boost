package i2f.core.io.nio.net.tcp;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * @author ltb
 * @date 2022/5/11 9:50
 * @desc
 */
public interface ITcpListener {

    void onRead(SocketChannel sc,ITcpConnector server) throws IOException;

    void onWrite(SocketChannel sc,ITcpConnector server) throws IOException;
}
