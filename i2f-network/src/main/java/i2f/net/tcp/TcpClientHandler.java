package i2f.net.tcp;

import java.io.IOException;

/**
 * @author Ice2Faith
 * @date 2024/3/11 15:13
 * @desc
 */
public interface TcpClientHandler {
    void onConnected(TcpClient tcpClient) throws IOException;

    default void onClosed(TcpClient tcpClient) throws IOException{

    }

    default void onException(Exception e,TcpClient tcpClient){
        e.printStackTrace();
    }
}
