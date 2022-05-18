package test.nio;

import i2f.core.nio.net.udp.IUdpConnector;
import i2f.core.nio.net.udp.IUdpListener;
import i2f.core.nio.net.udp.UdpServer;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author ltb
 * @date 2022/5/16 10:26
 * @desc
 */
public class TestUdpServer {
    public static void main(String[] args) throws IOException {
        UdpServer server=new UdpServer(19900, new IUdpListener() {
            @Override
            public void onRead(DatagramChannel sc, IUdpConnector server) throws IOException {
                byte[] bts=new byte[256];
                ByteBuffer buf=ByteBuffer.allocate(256);
                SocketAddress rmtAddr=sc.receive(buf);
                buf.flip();
                buf.get(bts,0,buf.limit());

                System.out.println("client["+rmtAddr+"]: "+new String(bts,0, buf.limit()));

            }

            @Override
            public void onWrite(DatagramChannel sc, IUdpConnector server) throws IOException {

            }
        });
        server.start();
    }
}
