package i2f.core.net.tcp;

import i2f.core.annotations.remark.Author;

import java.io.IOException;
import java.net.Socket;

@Author("i2f")
public class TcpClient extends SocketTransfer{
    private String ip;
    private int port;
    public TcpClient(String ip,int port) throws IOException {
        this.ip=ip;
        this.port=port;
        sock=new Socket(ip,port);
    }
    public void close() throws IOException {
        if(!sock.isClosed()){
            sock.close();
        }
    }
}
