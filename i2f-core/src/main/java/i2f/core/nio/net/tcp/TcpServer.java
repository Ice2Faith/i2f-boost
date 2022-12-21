package i2f.core.nio.net.tcp;

import lombok.Data;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author ltb
 * @date 2022/5/11 9:24
 * @desc
 */
@Data
public class TcpServer implements ITcpConnector {
    public static final String CTX_KEY_ADDR = "addr";
    public static final String CTX_KEY_PORT = "port";
    protected int port;
    protected ServerSocketChannel serverChannel;
    protected Selector selector;
    protected ITcpServerListener listener;
    protected CountDownLatch latch;
    protected ConcurrentHashMap<SocketChannel, Map<String, Object>> clientsMap = new ConcurrentHashMap<>();

    public TcpServer(int port, ITcpServerListener listener) {
        this.port = port;
        this.listener = listener;
        latch = new CountDownLatch(1);
    }
    @Override
    public ITcpConnector start() throws IOException {
        selector=Selector.open();

        serverChannel =ServerSocketChannel.open();
        serverChannel.bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        listener.onBind(serverChannel,this);

        serverChannel.register(selector,SelectionKey.OP_ACCEPT,null);

        latch.countDown();
        while(true){
            int cnt=selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator=keys.iterator();
            while(iterator.hasNext()){
                SelectionKey item= iterator.next();
                try{
                    if(item.isAcceptable()) {
                        ServerSocketChannel ssc = (ServerSocketChannel) item.channel();
                        SocketChannel sc = ssc.accept();
                        if (sc.isConnectionPending()) {
                            sc.finishConnect();
                        }
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ, null);

                        listener.onAccept(sc, this);
                        InetSocketAddress addr = (InetSocketAddress) sc.getRemoteAddress();
                        Map<String, Object> scContext = new HashMap<>();
                        scContext.put(CTX_KEY_ADDR, addr.getAddress().getHostAddress());
                        scContext.put(CTX_KEY_PORT, addr.getPort());
                        clientsMap.put(sc, scContext);
                    }else if(item.isReadable()){
                        SocketChannel sc = (SocketChannel) item.channel();
                        try{
                            listener.onRead(sc, this);
                        }catch(IOException e) {
                            boolean closed = NioSocketClosedResolver.resolveIoException(sc, e);
                            if (closed) {
                                listener.onClosed(sc, this);
                                clientsMap.remove(sc);
                            }
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

    public TcpServer await() throws InterruptedException {
        latch.await();
        return this;
    }
}
