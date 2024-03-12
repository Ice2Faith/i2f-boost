package i2f.net.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/12 8:52
 * @desc
 */
public class UdpServer implements Closeable {
    protected InetAddress bindAddr;
    protected int port=81;
    protected int maxPackSize=65530;
    protected DatagramSocket serverSocket;
    protected UdpServerHandler handler;
    protected AtomicBoolean started=new AtomicBoolean();

    public UdpServer(int port, UdpServerHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public UdpServer(InetAddress bindAddr, int port, UdpServerHandler handler) {
        this.bindAddr = bindAddr;
        this.port = port;
        this.handler = handler;
    }

    public UdpServer(InetAddress bindAddr, int port, int maxPackSize, UdpServerHandler handler) {
        this.bindAddr = bindAddr;
        this.port = port;
        this.maxPackSize = maxPackSize;
        this.handler = handler;
    }

    public void listen() throws IOException {
        if(started.get()){
            return;
        }
        this.serverSocket=new DatagramSocket(port,bindAddr);
        handler.onListening(this);
        Thread thread = new Thread(() -> {
            try{
                while(true){
                    byte[] buff=new byte[maxPackSize];
                    DatagramPacket packet=new DatagramPacket(buff,maxPackSize);
                    serverSocket.receive(packet);
                    byte[] data = packet.getData();
                    int offset = packet.getOffset();
                    int length = packet.getLength();
                    byte[] packetData = Arrays.copyOfRange(data, offset, offset + length);
                    handler.onMessage(packetData,packet,this);
                }
            }catch(Exception e){
                try{
                    if(e instanceof SocketException){
                        this.serverSocket.close();
                        handler.onServerClosed(this);
                    }
                }catch(Exception ex){

                }
                handler.onServerException(e,this);
            }
            try{
                if(this.serverSocket.isClosed()){
                    handler.onServerClosed(this);
                }
            }catch(Exception e){
                handler.onServerException(e,this);
            }
        }, "udp-server-main-loop");

        thread.start();

        started.set(true);
    }

    @Override
    public void close() throws IOException {
        if(!started.get()){
            return;
        }
        if(this.serverSocket==null){
            return;
        }
        if(this.serverSocket.isClosed()){
            return;
        }
        this.serverSocket.close();
        handler.onServerClosed(this);
        this.serverSocket=null;
        started.set(false);
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public AtomicBoolean getStarted() {
        return started;
    }

    public InetAddress getBindAddr() {
        return bindAddr;
    }

    public void setBindAddr(InetAddress bindAddr) {
        this.bindAddr = bindAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxPackSize() {
        return maxPackSize;
    }

    public void setMaxPackSize(int maxPackSize) {
        this.maxPackSize = maxPackSize;
    }

    public UdpServerHandler getHandler() {
        return handler;
    }

    public void setHandler(UdpServerHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UdpServer udpServer = (UdpServer) o;
        return port == udpServer.port &&
                maxPackSize == udpServer.maxPackSize &&
                Objects.equals(bindAddr, udpServer.bindAddr) &&
                Objects.equals(serverSocket, udpServer.serverSocket) &&
                Objects.equals(handler, udpServer.handler) &&
                Objects.equals(started, udpServer.started);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindAddr, port, maxPackSize, serverSocket, handler, started);
    }

    @Override
    public String toString() {
        return "UdpServer{" +
                "bindAddr=" + bindAddr +
                ", port=" + port +
                ", maxPackSize=" + maxPackSize +
                ", serverSocket=" + serverSocket +
                ", handler=" + handler +
                ", started=" + started +
                '}';
    }
}
