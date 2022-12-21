package i2f.extension.netty.tcp.test;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.consts.NettyFlag;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import i2f.extension.netty.tcp.tcp.NettyClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.logging.LogLevel;

import java.net.SocketAddress;
import java.util.Scanner;

public class TestNettyClient {
    public static void main(String[] args) {
        NettyClient client = new NettyClient("localhost", 8088, new ISocketChannelHandler() {

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                Channel channel = ctx.channel();
                new Thread(() -> {
                    Scanner scanner = new Scanner(System.in);

                    try {
                        while (true) {
                            System.out.println("input send text,input # exit:");
                            System.out.print(">/ ");
                            String line = scanner.nextLine();
                            if ("#".equals(line)) {
                                break;
                            }
                            byte flag = (byte) NettyFlag.NONE.code();
                            if (line.startsWith("@")) {
                                flag = (byte) NettyFlag.ECHO.code();
                                line = line.substring(1);
                            } else if (line.startsWith("!")) {
                                flag = (byte) NettyFlag.BROADCAST.code();
                                line = line.substring(1);
                            }
                            NettyPackage pkg = NettyPackages.ofUtf8(line);
                            pkg.flag = flag;
                            channel.writeAndFlush(pkg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    channel.close();
                }).start();
            }


            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                Object data = NettyPackages.parse((NettyPackage) msg);
                SocketAddress addr = ctx.channel().remoteAddress();
                System.out.println(addr + " send :" + data);
            }
        });

        client.setEnablePkgFlag(true);
        client.setLogLevel(LogLevel.DEBUG);
        client.setEnableHeartBeat(true);
        client.start();
    }
}
