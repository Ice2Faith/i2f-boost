package i2f.extension.flink.test.driver;


import i2f.core.io.nio.net.tcp.ITcpConnector;
import i2f.core.io.nio.net.tcp.ITcpServerListener;
import i2f.core.io.nio.net.tcp.TcpServer;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/11/14 10:53
 * @desc
 */
public class TcpTextServer {
    public static void main(String[] args) throws Exception {
        int port = 9999;
        String delimiter = "\n";
        if (args.length > 0) {
            try {
                Integer agPort = Integer.parseInt(args[0]);
                if (agPort != null) {
                    port = agPort.intValue();
                }
            } catch (Exception e) {

            }
        }
        if (args.length > 1) {
            delimiter = args[1];
        }

        TcpServer tcpServer = runServer(port);

        ByteBuffer buffer = ByteBuffer.allocate(8192);
        buffer.clear();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("请输入要发送的内容，#退出");
            String str = scanner.nextLine();
            if ("#".equals(str)) {
                break;
            }
            String text = str + delimiter;
            byte[] data = text.getBytes("UTF-8");
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            buffer.mark();
            for (SocketChannel item : tcpServer.getClientsMap().keySet()) {
                buffer.reset();
                item.write(buffer);
            }
        }
        tcpServer.getServerChannel().close();
    }

    public static TcpServer runServer(int port) {
        ITcpServerListener serverListener = new TextTcpServerListener();
        TcpServer server = new TcpServer(port, serverListener);
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                server.start();
                server.await();
            }
        }).start();
        return server;
    }

    public static class TextTcpServerListener implements ITcpServerListener {
        protected ByteBuffer buffer = ByteBuffer.allocate(8192);

        @Override
        public void onBind(ServerSocketChannel ssc, TcpServer server) throws IOException {
            System.out.println("server bind success on port: " + server.getPort());
        }

        @Override
        public void onAccept(SocketChannel sc, TcpServer server) throws IOException {
            InetSocketAddress addr = (InetSocketAddress) sc.getRemoteAddress();
            System.out.println("accept client:" + addr.getAddress().getHostAddress() + ":" + addr.getPort());
        }

        @Override
        public void onRead(SocketChannel sc, ITcpConnector server) throws IOException {
            InetSocketAddress addr = (InetSocketAddress) sc.getRemoteAddress();
            buffer.clear();
            sc.read(buffer);
            int len = buffer.limit();
            byte[] data = new byte[len];
            buffer.flip();
            buffer.get(data);
            String str = new String(data, "UTF-8");
            System.out.println("client " + addr.getAddress().getHostAddress() + ":" + addr.getPort() + " >/ " + str);
        }

        @Override
        public void onWrite(SocketChannel sc, ITcpConnector server) throws IOException {

        }

        @Override
        public void onClosed(SocketChannel sc, TcpServer server) throws IOException {
            Map<String, Object> ctx = server.getClientsMap().get(sc);
            System.out.println("closed client:" + ctx.get(TcpServer.CTX_KEY_ADDR) + ":" + ctx.get(TcpServer.CTX_KEY_PORT));
        }
    }
}
