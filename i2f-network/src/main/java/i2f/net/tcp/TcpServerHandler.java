package i2f.net.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Ice2Faith
 * @date 2024/3/11 14:51
 * @desc
 */
public interface TcpServerHandler {
    void onClientArrive(Socket clientSocket,TcpServer tcpServer) throws Exception;

    default void onServerClosed(TcpServer tcpServer) throws IOException{

    }

    default void onListening(TcpServer tcpServer) throws IOException{

    }

    default void onAccepting(TcpServer tcpServer) throws IOException{

    }

    default void onServerException(Exception e,TcpServer tcpServer){
        e.printStackTrace();
        try{
            tcpServer.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    default void onClientException(Socket clientSocket,Exception e,TcpServer tcpServer) throws IOException{
        e.printStackTrace();
        if(!clientSocket.isClosed()){
            clientSocket.close();
        }
    }
}
