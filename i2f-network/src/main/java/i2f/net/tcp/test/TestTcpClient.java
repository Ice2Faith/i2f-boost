package i2f.net.tcp.test;

import i2f.net.tcp.TcpClient;
import i2f.net.tcp.TcpClientHandler;
import i2f.net.tcp.impl.TcpClientSessionHandler;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * @author Ice2Faith
 * @date 2024/3/11 15:36
 * @desc
 */
public class TestTcpClient {
    public static void main(String[] args) throws Exception {
        TcpClient client=new TcpClient(Inet4Address.getByName("127.0.0.1"), 7080, new TcpClientSessionHandler() {
            @Override
            public void onClosed(TcpClient tcpClient) throws IOException {
                System.out.println("closed.");
            }

            @Override
            public void onLoopMessage(Socket socket, TcpClient tcpClient) throws Exception {
                SocketAddress remoteSocketAddress = tcpClient.getSocket().getRemoteSocketAddress();
                InputStream is = tcpClient.getSocket().getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println("server[" + remoteSocketAddress + "]:" + line);
                }
            }
        });

        client.connect();
        System.out.println("server[" + client.getRemoteAddr() + "] connected.");

        System.out.println("input # exit, else send.");
        OutputStream os = client.getSocket().getOutputStream();
        BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));
        Scanner scanner=new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            if("#".equals(line)){
                break;
            }
            writer.write(line);
            writer.newLine();
            writer.flush();
        }

        client.close();
    }
}
