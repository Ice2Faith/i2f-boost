package test.nio;

import i2f.core.nio.net.udp.UdpClient;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/5/16 10:34
 * @desc
 */
public class TestUdpClient {
    public static void main(String[] args) throws IOException {
        UdpClient client=new UdpClient("127.0.0.1",19900);
        client.start();
        Scanner scanner=new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            client.send(ByteBuffer.wrap(line.getBytes()));
        }
    }
}
