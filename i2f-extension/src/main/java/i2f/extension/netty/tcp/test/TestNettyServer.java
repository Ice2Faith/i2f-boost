package i2f.extension.netty.tcp.test;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import i2f.extension.netty.tcp.tcp.NettyServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;

import java.net.SocketAddress;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class TestNettyServer {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(8088, new ISocketChannelHandler() {
            private ConcurrentHashMap<Channel, Boolean> clients = new ConcurrentHashMap<>();

            @Override
            public void afterInitChannel(SocketChannel channel) throws Exception {
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
                            NettyPackage pkg = NettyPackages.ofUtf8(line);
                            for (Channel item : clients.keySet()) {
                                item.writeAndFlush(pkg);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }).start();
            }

            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                SocketAddress addr = ctx.channel().remoteAddress();
                System.out.println("online:" + addr);
                clients.put(ctx.channel(), true);
            }

            @Override
            public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                SocketAddress addr = ctx.channel().remoteAddress();
                System.out.println("offline:" + addr);
                clients.remove(ctx.channel());
            }

            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                Object data = NettyPackages.parse((NettyPackage) msg);
                SocketAddress addr = ctx.channel().remoteAddress();
                System.out.println(addr + " send :" + data);

                // echo back
//                ctx.channel().writeAndFlush(msg);
            }
        });

        server.setEnablePkgFlag(true);
        server.setLogLevel(LogLevel.DEBUG);
        server.setEnableHeartBeat(true);
        server.start();
    }
}
