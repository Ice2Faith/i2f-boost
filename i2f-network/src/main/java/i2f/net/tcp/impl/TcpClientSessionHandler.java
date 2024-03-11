package i2f.net.tcp.impl;

import i2f.net.tcp.TcpClient;
import i2f.net.tcp.TcpClientHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * @author Ice2Faith
 * @date 2024/3/11 16:48
 * @desc
 */
public abstract class TcpClientSessionHandler implements TcpClientHandler {

    @Override
    public void onConnected(TcpClient tcpClient) throws IOException {
        SocketAddress remoteSocketAddress = tcpClient.getSocket().getRemoteSocketAddress();
        Thread thread = new Thread(() -> {
            try {
                onLoopMessage(tcpClient.getSocket(), tcpClient);
            } catch (Exception e) {
                if(e instanceof SocketException){
                    try{
                        tcpClient.getSocket().close();
                    }catch(Exception ex){
                    }
                }
                tcpClient.getHandler().onException(e,tcpClient);
            }
        },"client-"+remoteSocketAddress);
        thread.start();
    }


    @Override
    public void onException(Exception e, TcpClient tcpClient) {
        if(tcpClient.getSocket().isClosed()){
            try{
                onClosed(tcpClient);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        e.printStackTrace();
    }

    public abstract void onLoopMessage(Socket socket, TcpClient tcpClient) throws Exception;
}
