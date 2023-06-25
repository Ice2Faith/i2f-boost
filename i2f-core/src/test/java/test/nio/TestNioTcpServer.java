package test.nio;

import i2f.core.io.nio.net.tcp.ITcpConnector;
import i2f.core.io.nio.net.tcp.ITcpServerListener;
import i2f.core.io.nio.net.tcp.TcpServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @author ltb
 * @date 2022/5/11 10:29
 * @desc
 */
public class TestNioTcpServer {
    public static void main(String[] args) throws IOException {
        TcpServer server=(TcpServer)new TcpServer(11990, new ITcpServerListener() {
            @Override
            public void onBind(ServerSocketChannel ssc, TcpServer server) throws IOException {
                System.out.println("server bind.");
            }

            @Override
            public void onAccept(SocketChannel sc, TcpServer server) throws IOException {
                System.out.println("client arrive:"+sc.getRemoteAddress());
                sc.write(ByteBuffer.wrap("hello".getBytes()));
            }

            @Override
            public void onRead(SocketChannel sc, ITcpConnector server)  throws IOException{
                byte[] bts=new byte[256];
                ByteBuffer buffer=ByteBuffer.allocate(256);
                int len=sc.read(buffer);
                buffer.flip();
                buffer.get(bts,0,len);
                String str=new String(bts,0,len);
                System.out.println("client(" + sc.getRemoteAddress() + ") say:" + str);

                buffer.rewind();
                sc.write(buffer);
            }

            @Override
            public void onWrite(SocketChannel sc, ITcpConnector server) throws IOException {

            }

            @Override
            public void onClosed(SocketChannel sc, TcpServer server) throws IOException {

            }
        }).start();
    }
}
