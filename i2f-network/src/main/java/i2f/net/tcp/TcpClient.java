package i2f.net.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/11 14:38
 * @desc
 */
public class TcpClient implements Closeable {
    protected InetAddress remoteAddr;
    protected int port=80;
    protected Socket socket;
    protected TcpClientHandler handler;
    protected AtomicBoolean started=new AtomicBoolean();

    public TcpClient(InetAddress remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public TcpClient(InetAddress remoteAddr, int port) {
        this.remoteAddr = remoteAddr;
        this.port = port;
    }

    public TcpClient(InetAddress remoteAddr, int port, TcpClientHandler handler) {
        this.remoteAddr = remoteAddr;
        this.port = port;
        this.handler = handler;
    }

    public void connect() throws IOException {
        if(started.get()){
            return;
        }
        socket=new Socket(remoteAddr,port);
        handler.onConnected(this);
        started.set(true);
    }

    @Override
    public void close() throws IOException {
        if(!started.get()){
            return;
        }
        if(this.socket==null){
            return;
        }
        if(this.socket.isClosed()){
            return;
        }
        this.socket.close();
        handler.onClosed(this);
        this.socket=null;
        started.set(false);
    }

    public Socket getSocket() {
        return socket;
    }

    public AtomicBoolean getStarted() {
        return started;
    }

    public InetAddress getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(InetAddress remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public TcpClientHandler getHandler() {
        return handler;
    }

    public void setHandler(TcpClientHandler handler) {
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
        TcpClient tcpClient = (TcpClient) o;
        return port == tcpClient.port &&
                Objects.equals(remoteAddr, tcpClient.remoteAddr) &&
                Objects.equals(socket, tcpClient.socket) &&
                Objects.equals(handler, tcpClient.handler) &&
                Objects.equals(started, tcpClient.started);
    }

    @Override
    public int hashCode() {
        return Objects.hash(remoteAddr, port, socket, handler, started);
    }

    @Override
    public String toString() {
        return "TcpClient{" +
                "remoteAddr=" + remoteAddr +
                ", port=" + port +
                ", socket=" + socket +
                ", handler=" + handler +
                ", started=" + started +
                '}';
    }
}
