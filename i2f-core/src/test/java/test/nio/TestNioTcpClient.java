package test.nio;

import i2f.core.io.nio.net.tcp.ITcpClientListener;
import i2f.core.io.nio.net.tcp.ITcpConnector;
import i2f.core.io.nio.net.tcp.TcpClient;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/5/11 10:29
 * @desc
 */
public class TestNioTcpClient {
    public static void main(String[] args) throws IOException, InterruptedException {
        TcpClient client=(TcpClient)new TcpClient("127.0.0.1", 11990, new ITcpClientListener() {
            @Override
            public void onConnect(SocketChannel sc,TcpClient client) {
                System.out.println("connect server.");
            }

            @Override
            public void onRead(SocketChannel sc, ITcpConnector server)  throws IOException {
                byte[] bts=new byte[256];
                ByteBuffer buffer=ByteBuffer.allocate(256);
                int len=sc.read(buffer);
                buffer.flip();
                buffer.get(bts,0,len);
                String str=new String(bts,0,len);
                System.out.println("server say:"+str);
            }

            @Override
            public void onWrite(SocketChannel sc, ITcpConnector server) throws IOException {

            }
        });

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                client.start();
            }
        }).start();

        client.await();

        Scanner scanner=new Scanner(System.in);

        while(true){
            String str="aaa";//scanner.nextLine();

            ByteBuffer buffer=ByteBuffer.allocate(256);
            buffer.clear();
            buffer.put(str.getBytes());
            buffer.flip();
            client.getChannel().write(buffer);
        }
    }
}
