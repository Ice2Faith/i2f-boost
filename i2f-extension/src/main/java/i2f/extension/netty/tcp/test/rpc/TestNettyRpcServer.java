package i2f.extension.netty.tcp.test.rpc;

import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.rpc.NettyRpcServer;
import i2f.extension.netty.tcp.rpc.handler.InstanceBeanSupplier;
import i2f.extension.netty.tcp.rpc.handler.RpcServerSocketChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;

public class TestNettyRpcServer {
    public static void main(String[] args) {
        InstanceBeanSupplier beanSupplier = new InstanceBeanSupplier();
        beanSupplier.registerBean(new RpcServiceImpl());

        NettyRpcServer server = NettyRpcServer.instance(9999, new RpcServerSocketChannelHandler(beanSupplier) {
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

        server.setEnableHeartBeat(true);
        server.setLogLevel(LogLevel.DEBUG);
        server.setEnablePkgFlag(false);

        server.start();
    }
}
