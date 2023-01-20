package i2f.extension.netty.tcp.tcp;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.NettyProtocol;
import i2f.extension.netty.tcp.protocol.consts.NettyFlag;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * 作为TCP的客户端
 * 主要就是
 * 启用连接，连接到服务器
 * 启动event-loop进行事件处理
 * 负责组装协议组件
 * 完成协议包的处理
 * 包含，协议的flag的处理，心跳的处理等
 */
@Getter
public class NettyClient extends NettyTcpEndPoint {

    protected String host;

    protected ISocketChannelHandler handler;

    public NettyClient(String host, ISocketChannelHandler handler) {
        this.host = host;
        this.handler = handler;
    }

    public NettyClient(String host, int port, ISocketChannelHandler handler) {
        this.host = host;
        this.port = port;
        this.handler = handler;
    }


    protected LoggingHandler loggingHandler;
    protected NioEventLoopGroup group;
    protected Bootstrap bootstrap;

    protected Channel channel;

    @Override
    public void start() {
        if (loggingHandler == null && logLevel != null) {
            loggingHandler = new LoggingHandler(logLevel);
        }

        group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            handler.initBootstrap(bootstrap);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    if (enableHeartBeat) {
                        channel.pipeline().addLast("idle monitor", new IdleStateHandler(3 * idleTimeoutSeconds, 1 * idleTimeoutSeconds, 0, TimeUnit.SECONDS));
                    }
                    NettyProtocol.initChannel(channel, maxFrameLength);
                    if (loggingHandler != null) {
                        channel.pipeline().addLast(loggingHandler);
                    }
                    if (enableHeartBeat) {
                        channel.pipeline().addLast("idle handler", new ChannelDuplexHandler() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                if (evt == IdleStateEvent.WRITER_IDLE_STATE_EVENT) {
                                    if (isEnableLogging()) {
                                        logger.debug("keep heart beat with server.");
                                    }
                                    ctx.channel().writeAndFlush(NettyPackages.HEART_BEAT_PKG);
                                }
                                if (evt == IdleStateEvent.READER_IDLE_STATE_EVENT) {
                                    if (isEnableLogging()) {
                                        SocketAddress addr = ctx.channel().remoteAddress();
                                        logger.debug(addr + " not heart beat idle timeout, close connection.");
                                    }
                                    ctx.channel().close();
                                }
                            }
                        });
                    }
                    handler.beforeInitChannel(channel);
                    channel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            handler.channelActive(ctx);

                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            handler.channelInactive(ctx);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (enableHeartBeat) {
                                if (msg instanceof NettyPackage) {
                                    NettyPackage pkg = (NettyPackage) msg;
                                    if (pkg.flag == NettyFlag.HEART_BEAT.code()) {
                                        if (isEnableLogging()) {
                                            logger.debug("server response heart beat.");
                                        }
                                        return;
                                    }
                                }
                            }
                            if (enablePkgFlag) {
                                NettyPackage pkg = (NettyPackage) msg;
                                if (pkg.flag == NettyFlag.ECHO.code()) {
                                    ctx.writeAndFlush(msg);
                                } else {
                                    handler.channelRead(ctx, msg);
                                }
                            } else {
                                handler.channelRead(ctx, msg);
                            }
                        }
                    });
                    handler.afterInitChannel(channel);
                }
            });
            ChannelFuture future = bootstrap.connect(host, port).sync();
            channel = future.channel();
            future.channel().closeFuture().addListener(promise -> {
                group.shutdownGracefully();
            });
        } catch (Exception e) {
            if (isEnableLogging()) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
