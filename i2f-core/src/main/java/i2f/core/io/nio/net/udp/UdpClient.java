package i2f.core.io.nio.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author ltb
 * @date 2022/5/16 10:17
 * @desc
 */
public class UdpClient implements IUdpConnector{
    protected String host;
    protected int port;
    protected DatagramChannel channel;
    protected SocketAddress address;
    public UdpClient(String host,int port){
        this.host=host;
        this.port=port;
    }
    @Override
    public IUdpConnector start() throws IOException {
        channel=DatagramChannel.open();
        address=new InetSocketAddress(host,port);
        return this;
    }
    public int send(ByteBuffer buf) throws IOException {
        int sentCnt=channel.send(buf,address);
        return sentCnt;
    }
}
