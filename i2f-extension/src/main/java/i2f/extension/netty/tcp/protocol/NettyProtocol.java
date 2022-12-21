package i2f.extension.netty.tcp.protocol;

import i2f.extension.netty.tcp.protocol.codec.NettyPackageCodec;
import i2f.extension.netty.tcp.protocol.codec.NettyPackageFrameDecoder;
import io.netty.channel.socket.SocketChannel;

/**
 * 提供了对协议包的进一步封装
 * 目的是避免用户错误使用导致的不正确的结果
 */
public class NettyProtocol {
    /**
     * 共享的包编解码器
     */
    public static NettyPackageCodec sharedCodec = new NettyPackageCodec();

    /**
     * 负责进行初始化一个channel来支持协议包
     *
     * @param channel
     * @param maxFrameLength
     * @return
     */
    public static SocketChannel initChannel(SocketChannel channel, int maxFrameLength) {
        channel.pipeline().addLast("frame-decoder", new NettyPackageFrameDecoder(maxFrameLength));
        channel.pipeline().addLast("package-codec", sharedCodec);
        return channel;
    }
}
