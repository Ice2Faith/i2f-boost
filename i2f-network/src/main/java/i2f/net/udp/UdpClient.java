package i2f.net.udp;

import java.io.Closeable;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Ice2Faith
 * @date 2024/3/12 9:15
 * @desc
 */
public class UdpClient implements Closeable {
    public static final UdpClientHandler EMPTY_HANDLER=new UdpClientHandler() {};
    protected InetAddress remoteAddr;
    protected int port=81;
    protected DatagramSocket socket;
    protected UdpClientHandler handler;

    public UdpClient(InetAddress remoteAddr, int port) throws IOException {
        this.remoteAddr = remoteAddr;
        this.port = port;
        this.handler = EMPTY_HANDLER;
        prepare();
    }

    public UdpClient(InetAddress remoteAddr, int port, UdpClientHandler handler) throws IOException {
        this.remoteAddr = remoteAddr;
        this.port = port;
        this.handler = handler;
        prepare();
    }

    public void prepare() throws IOException {
        this.socket=new DatagramSocket();
        handler.onPrepared(socket,this);
    }

    public void send(DatagramPacket packet) throws IOException {
        socket.send(packet);
    }

    public void send(byte[] data) throws IOException {
        DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,port);
        socket.send(packet);
    }

    public void send(byte[] data,InetAddress remoteAddr,int port) throws IOException {
        DatagramPacket packet=new DatagramPacket(data,data.length,remoteAddr,port);
        socket.send(packet);
    }

    @Override
    public void close() throws IOException {
        if(this.socket==null){
            return;
        }
        if(this.socket.isClosed()){
            return;
        }
        this.socket.close();
        handler.onClosed(this);
        this.socket=null;
    }
}
