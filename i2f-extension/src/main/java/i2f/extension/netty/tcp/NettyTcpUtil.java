package i2f.extension.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * @author ltb
 * @date 2022/6/24 18:26
 * @desc
 */
public class NettyTcpUtil {

    public static Channel starterServer(int port) throws InterruptedException {
        return new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        System.out.println("client accept:"+channel.remoteAddress().getAddress());
                        channel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        InetSocketAddress addr = (InetSocketAddress)ctx.channel().remoteAddress();
                                        System.out.println("client "+addr.getAddress().getHostAddress()+"/"+addr.getPort()+":"+msg);
                                        ctx.channel().writeAndFlush("echo "+msg);
                                    }
                                });
                    }
                })
                .bind(port)
                .channel();
    }

    public static Channel startClient(String host,int port) throws InterruptedException {
        return new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel channel) throws Exception {
                        channel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        InetSocketAddress addr = (InetSocketAddress)ctx.channel().remoteAddress();
                                        System.out.println("server "+addr.getAddress().getHostAddress()+"/"+addr.getPort()+":"+msg);
                                    }
                                });
                    }
                })
                .connect(new InetSocketAddress(host,port))
                .sync()
                .channel();
    }
}
