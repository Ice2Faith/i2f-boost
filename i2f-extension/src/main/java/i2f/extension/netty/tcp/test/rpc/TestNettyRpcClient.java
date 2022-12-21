package i2f.extension.netty.tcp.test.rpc;

import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.rpc.NettyRpcClient;
import i2f.extension.netty.tcp.rpc.handler.RpcClientSocketChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;

public class TestNettyRpcClient {
    public static void main(String[] args) throws Exception {

        NettyRpcClient client = NettyRpcClient.instance("localhost", 9999,
                new RpcClientSocketChannelHandler() {
                    @Override
                    public void beforeInitChannel(SocketChannel channel) throws Exception {
                        channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                NettyPackage pkg = (NettyPackage) msg;
                                for (int i = 0; i < pkg.content.length; i++) {
                                    pkg.content[i] = (byte) ((pkg.content[i] ^ 0x73) & 0x0ff);
                                }
                                ctx.fireChannelRead(pkg);
                            }
                        });
                        channel.pipeline().addLast(new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                NettyPackage pkg = (NettyPackage) msg;
                                for (int i = 0; i < pkg.content.length; i++) {
                                    pkg.content[i] = (byte) ((pkg.content[i] ^ 0x73) & 0x0ff);
                                }
                                ctx.write(pkg, promise);
                            }
                        });
                    }
                });

        client.setEnableHeartBeat(true);
        client.setLogLevel(LogLevel.DEBUG);
        client.setEnablePkgFlag(false);

        client.start();

        IRpcService service = client.proxy(IRpcService.class);

        boolean ok = service.login("admin", "123456");

        System.out.println(ok);

        User root = service.getUserByName("root");

        System.out.println(root);

        User user = service.updateUser(root);

        System.out.println(user);

        User user1 = service.exceptionUser(root);

        System.out.println(user1);
    }
}
