package i2f.net.udp.test;

import i2f.net.udp.UdpClient;
import i2f.net.udp.UdpClientHandler;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * @author Ice2Faith
 * @date 2024/3/12 9:30
 * @desc
 */
public class TestUdpClient {
    public static void main(String[] args) throws IOException {
        UdpClient udpClient=new UdpClient(InetAddress.getByName("127.0.0.1"), 81, new UdpClientHandler() {
            @Override
            public void onPrepared(DatagramSocket socket, UdpClient udpClient) throws IOException {
                System.out.println("prepared.");
            }

            @Override
            public void onClosed(UdpClient udpClient) throws IOException {
                System.out.println("closed.");
            }
        });

        Scanner scanner=new Scanner(System.in);
        System.out.println("input # exit, else send.");
        while(true){
            String line=scanner.nextLine();
            if("#".equals(line)){
                break;
            }
            udpClient.send(line.getBytes());
        }

        udpClient.close();
    }
}
