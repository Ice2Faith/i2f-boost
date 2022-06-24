package i2f.extension.netty.tcp;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * @author ltb
 * @date 2022/6/24 18:59
 * @desc
 */
public class TestNettyTcpClient {
    public static void main(String[] args) throws InterruptedException {
        Channel channel=NettyTcpUtil.startClient("localhost",9110);
        Scanner scanner=new Scanner(System.in);
        String line=null;
        while(true){
            System.out.println("input # to exit else send msg:");

            line=scanner.nextLine();
            if("#".equals(line)){
                break;
            }
            channel.writeAndFlush(line);
        }
        channel.close();
    }
}
