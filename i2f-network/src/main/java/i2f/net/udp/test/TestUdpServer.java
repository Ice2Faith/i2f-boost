package i2f.net.udp.test;

import i2f.net.udp.UdpClient;
import i2f.net.udp.UdpClientHandler;
import i2f.net.udp.UdpServer;
import i2f.net.udp.UdpServerHandler;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 * @author Ice2Faith
 * @date 2024/3/12 9:29
 * @desc
 */
public class TestUdpServer {
    public static void main(String[] args) throws IOException {
        UdpServer udpServer=new UdpServer(81, new UdpServerHandler() {
            @Override
            public void onServerClosed(UdpServer udpServer) throws IOException {
                System.out.println("closed.");
            }

            @Override
            public void onListening(UdpServer udpServer) throws IOException {
                System.out.println("listening...");
            }

            @Override
            public void onMessage(byte[] data, DatagramPacket packet, UdpServer udpServer) throws IOException {
                String str=new String(data);
                System.out.println("client["+packet.getSocketAddress()+"]:"+str);
            }
        });

        udpServer.listen();

    }
}
