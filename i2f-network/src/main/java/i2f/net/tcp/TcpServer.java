package i2f.net.tcp;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Ice2Faith
 * @date 2024/3/11 14:37
 * @desc
 */
public class TcpServer implements Closeable {
    protected InetAddress bindAddr;
    protected int port=80;
    protected int backlog=50;
    protected ServerSocket serverSocket;
    protected TcpServerHandler handler;
    protected AtomicBoolean started=new AtomicBoolean();

    public TcpServer(TcpServerHandler handler) {
        this.handler = handler;
    }

    public TcpServer(int port, TcpServerHandler handler) {
        this.port = port;
        this.handler = handler;
    }

    public TcpServer(InetAddress bindAddr, int port, TcpServerHandler handler) {
        this.bindAddr = bindAddr;
        this.port = port;
        this.handler = handler;
    }

    public TcpServer(InetAddress bindAddr, int port, int backlog, TcpServerHandler handler) {
        this.bindAddr = bindAddr;
        this.port = port;
        this.backlog = backlog;
        this.handler = handler;
    }

    public void listen() throws IOException {
        if(started.get()){
            return;
        }
        this.serverSocket=new ServerSocket(port,backlog,bindAddr);
        handler.onListening(this);
        Thread thread = new Thread(() -> {
            try{
                handler.onAccepting(this);
                while (!serverSocket.isClosed()) {
                    Socket clientSocket = serverSocket.accept();
                    try{
                        handler.onClientArrive(clientSocket,this);
                    }catch(Exception e){
                        handler.onClientException(clientSocket,e,this);
                    }
                }
            }catch(Exception e){
                handler.onServerException(e,this);
            }
            try{
                if(this.serverSocket.isClosed()){
                    handler.onServerClosed(this);
                }
            }catch(Exception e){
                handler.onServerException(e,this);
            }
        }, "tcp-server-main-loop");

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

    public ServerSocket getServerSocket() {
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

    public int getBacklog() {
        return backlog;
    }

    public void setBacklog(int backlog) {
        this.backlog = backlog;
    }

    public TcpServerHandler getHandler() {
        return handler;
    }

    public void setHandler(TcpServerHandler handler) {
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
        TcpServer tcpServer = (TcpServer) o;
        return port == tcpServer.port &&
                backlog == tcpServer.backlog &&
                Objects.equals(bindAddr, tcpServer.bindAddr) &&
                Objects.equals(serverSocket, tcpServer.serverSocket) &&
                Objects.equals(handler, tcpServer.handler) &&
                Objects.equals(started, tcpServer.started);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindAddr, port, backlog, serverSocket, handler, started);
    }

    @Override
    public String toString() {
        return "TcpServer{" +
                "bindAddr=" + bindAddr +
                ", port=" + port +
                ", backlog=" + backlog +
                ", serverSocket=" + serverSocket +
                ", accepter=" + handler +
                ", started=" + started +
                '}';
    }
}
