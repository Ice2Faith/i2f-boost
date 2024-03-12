package i2f.net.udp;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @author Ice2Faith
 * @date 2024/3/12 8:54
 * @desc
 */
public interface UdpServerHandler {

    default void onServerClosed(UdpServer udpServer) throws IOException {

    }

    default void onListening(UdpServer udpServer) throws IOException{

    }

    void onMessage(byte[] data,DatagramPacket packet,UdpServer udpServer) throws IOException;

    default void onServerException(Exception e,UdpServer tcpServer){
        e.printStackTrace();
        try{
            tcpServer.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

}
