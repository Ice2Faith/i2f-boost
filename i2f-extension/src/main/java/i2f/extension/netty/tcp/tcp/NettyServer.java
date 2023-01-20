package i2f.extension.netty.tcp.tcp;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.NettyProtocol;
import i2f.extension.netty.tcp.protocol.consts.NettyFlag;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.Getter;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 作为TCP的服务端
 * 主要就是
 * 启用连接，连接到服务器
 * 启动event-loop进行事件处理
 * 负责组装协议组件
 * 完成协议包的处理
 * 包含，协议的flag的处理，心跳的处理等
 */
@Getter
public class NettyServer extends NettyTcpEndPoint {

    protected ISocketChannelHandler handler;

    public NettyServer(ISocketChannelHandler handler) {
        this.handler = handler;
    }

    public NettyServer(int port, ISocketChannelHandler handler) {
        this.port = port;
        this.handler = handler;
    }


    protected LoggingHandler loggingHandler;
    protected NioEventLoopGroup boss;
    protected NioEventLoopGroup worker;
    protected ServerBootstrap serverBootstrap;

    private ConcurrentHashMap<Channel, Boolean> clients = new ConcurrentHashMap<>();

    @Override
    public void start() {
        if (loggingHandler == null && logLevel != null) {
            loggingHandler = new LoggingHandler(logLevel);
        }

        boss = new NioEventLoopGroup();
        worker = new NioEventLoopGroup();
        try {
            serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            handler.initServerBootstrap(serverBootstrap);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    if (enableHeartBeat) {
                        channel.pipeline().addLast("idle monitor", new IdleStateHandler(3 * idleTimeoutSeconds, 0, 0, TimeUnit.SECONDS));
                    }
                    NettyProtocol.initChannel(channel, maxFrameLength);
                    if (loggingHandler != null) {
                        channel.pipeline().addLast(loggingHandler);
                    }
                    if (enableHeartBeat) {
                        channel.pipeline().addLast("idle handler", new ChannelDuplexHandler() {
                            @Override
                            public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
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
                            clients.put(ctx.channel(), true);
                            handler.channelActive(ctx);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            handler.channelInactive(ctx);
                            clients.remove(ctx.channel());
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            if (enableHeartBeat) {
                                if (msg instanceof NettyPackage) {
                                    NettyPackage pkg = (NettyPackage) msg;
                                    if (pkg.flag == NettyFlag.HEART_BEAT.code()) {
                                        if (isEnableLogging()) {
                                            SocketAddress addr = ctx.channel().remoteAddress();
                                            logger.debug(addr + " heart beat.");
                                        }
                                        ctx.writeAndFlush(msg);
                                        return;
                                    }
                                }
                            }
                            if (enablePkgFlag) {
                                NettyPackage pkg = (NettyPackage) msg;
                                if (pkg.flag == NettyFlag.ECHO.code()) {
                                    ctx.writeAndFlush(msg);
                                } else if (pkg.flag == NettyFlag.BROADCAST.code()) {
                                    for (Channel item : clients.keySet()) {
                                        item.writeAndFlush(msg);
                                    }
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
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            if (isEnableLogging()) {
                logger.error(e.getMessage(), e);
            }
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
