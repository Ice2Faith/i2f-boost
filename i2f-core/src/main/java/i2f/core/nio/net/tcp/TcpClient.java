package i2f.core.nio.net.tcp;

import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/5/11 10:16
 * @desc
 */
@Data
public class TcpClient implements ITcpConnector{
    protected String host;
    protected int port;
    protected SocketChannel channel;
    protected ITcpClientListener listener;
    protected Selector selector;
    protected CountDownLatch latch;
    public TcpClient(String host,int port,ITcpClientListener listener){
        latch=new CountDownLatch(1);
        this.host=host;
        this.port=port;
        this.listener=listener;
    }
    @Override
    public ITcpConnector start() throws IOException {
        selector=Selector.open();

        channel =SocketChannel.open();
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_CONNECT|SelectionKey.OP_READ|SelectionKey.OP_WRITE, null);

        channel.connect(new InetSocketAddress(host,port));

        while(true){
            int cnt=selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator=keys.iterator();
            while(iterator.hasNext()){
                SelectionKey item= iterator.next();
                try{
                    if(item.isConnectable()){
                        SocketChannel channel = (SocketChannel) item.channel();
                        if (channel.isConnectionPending()) {
                            channel.finishConnect();
                        }
                        latch.countDown();
                        listener.onConnect(this.channel,this);
                    }else if(item.isReadable()){
                        SocketChannel sc = (SocketChannel) item.channel();
                        try{
                            listener.onRead(sc,this);
                        }catch(IOException e){
                            NioSocketClosedResolver.resolveIoException(sc,e);
                        }
                    }else if(item.isWritable()){
                        SocketChannel sc = (SocketChannel) item.channel();
                        listener.onWrite(sc,this);
                    }
                }catch(Exception e){
                    item.cancel();
                    e.printStackTrace();
                }
                iterator.remove();
            }
        }

    }

    public TcpClient await() throws InterruptedException {
        latch.await();
        return this;
    }
}
