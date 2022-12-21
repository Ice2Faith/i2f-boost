package i2f.extension.netty.tcp.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.SocketChannel;

public interface ISocketChannelHandler {
    /**
     * 用于提供初始化服务端的其他操作
     *
     * @param serverBootstrap
     */
    default void initServerBootstrap(ServerBootstrap serverBootstrap) {

    }

    /**
     * 用于提供初始化客户端的其他操作
     *
     * @param bootstrap
     */
    default void initBootstrap(Bootstrap bootstrap) {

    }

    /**
     * 用于提供前置的数据处理
     * 例如进出站的加解密
     *
     * @param channel
     * @throws Exception
     */
    default void beforeInitChannel(SocketChannel channel) throws Exception {

    }

    /**
     * 用于提供后置的数据处理
     * 例如后续的更多处理
     *
     * @param channel
     * @throws Exception
     */
    default void afterInitChannel(SocketChannel channel) throws Exception {

    }

    /**
     * 连接建立时
     * 在客户端表示连接成功
     * 在服务端表示有客户端连接上来
     *
     * @param ctx
     * @throws Exception
     */
    default void channelActive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 连接断开时
     * 在客户端表示连接断开
     * 在服务端表示有客户端连接断开
     *
     * @param ctx
     * @throws Exception
     */
    default void channelInactive(ChannelHandlerContext ctx) throws Exception {

    }

    /**
     * 有数据读取时
     * 在客户端和服务端都是等价的
     * 都是对方发送来了数据
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    default void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
