package i2f.core.nio.net.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;

/**
 * @author ltb
 * @date 2022/5/16 10:07
 * @desc
 */
public class UdpServer implements IUdpConnector{
    protected int port;
    protected DatagramChannel channel;
    protected Selector selector;
    protected IUdpListener listener;
    public UdpServer(int port,IUdpListener listener){
        this.port=port;
        this.listener=listener;
    }
    @Override
    public IUdpConnector start() throws IOException {
        selector=Selector.open();
        channel=DatagramChannel.open();
        channel.bind(new InetSocketAddress(port));
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE);

        while(true){
            int cnt= selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()){
                SelectionKey key= iterator.next();
                try{
                    if(key.isReadable()){
                        DatagramChannel cnl = (DatagramChannel) key.channel();
                        listener.onRead(cnl,this);
                    }else if(key.isWritable()){
                        DatagramChannel cnl = (DatagramChannel) key.channel();
                        listener.onWrite(cnl,this);
                    }
                }catch(Exception e){
                    key.cancel();
                    e.printStackTrace();
                }
                iterator.remove();
            }
        }
    }
}
