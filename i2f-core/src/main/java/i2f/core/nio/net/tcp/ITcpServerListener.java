package i2f.core.nio.net.tcp;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ltb
 * @date 2022/5/11 9:59
 * @desc
 */
public interface ITcpServerListener extends ITcpListener{
    void onBind(ServerSocketChannel ssc, TcpServer server) throws IOException;

    void onAccept(SocketChannel sc, TcpServer server) throws IOException;
}
