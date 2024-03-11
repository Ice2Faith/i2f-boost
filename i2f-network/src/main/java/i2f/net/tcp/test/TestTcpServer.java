package i2f.net.tcp.test;

import i2f.net.tcp.TcpServer;
import i2f.net.tcp.impl.TcpServerSessionHandler;

import java.io.*;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Ice2Faith
 * @date 2024/3/11 15:17
 * @desc
 */
public class TestTcpServer {
    public static void main(String[] args) throws IOException {
        TcpServer server=new TcpServer(7080, new TcpServerSessionHandler() {
            @Override
            public void onLoopMessage(Socket clientSocket, String sessionId, Map<String, Object> session, TcpServer tcpServer) throws Exception {
                SocketAddress remoteSocketAddress = clientSocket.getRemoteSocketAddress();
                InputStream is = clientSocket.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                OutputStream os = clientSocket.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(os));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println("client[" + remoteSocketAddress + "]:" + line);
                    writer.write("echo:"+line);
                    writer.newLine();
                    writer.flush();
                }
            }

            @Override
            public void onClientClosed(Socket clientSocket, String sessionId, Map<String, Object> session, Exception e, TcpServer tcpServer){
                SocketAddress remoteSocketAddress = clientSocket.getRemoteSocketAddress();
                System.out.println("client[" + remoteSocketAddress + "] closed.");
            }
        });

        server.listen();

        TcpServerSessionHandler handler = (TcpServerSessionHandler) server.getHandler();

        System.out.println("input # exit, else send.");
        Scanner scanner=new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            if("#".equals(line)){
                break;
            }
            for (Socket onlineSocket : handler.getOnlineSockets()) {
                OutputStream os = onlineSocket.getOutputStream();
                os.write(line.getBytes());
                os.write('\n');
                os.flush();
            }
        }

        server.close();
    }
}
