package i2f.extension.netty.tcp.protocol.codec;

import i2f.extension.netty.tcp.protocol.consts.NettyConst;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 根据协议包适配的帧处理器
 * 负责处理粘包和半包
 */
public class NettyPackageFrameDecoder extends LengthFieldBasedFrameDecoder {
    // 默认64K大小
    public NettyPackageFrameDecoder() {
        this(NettyConst.DEFAULT_MAX_FRAME_LENGTH);
    }

    public NettyPackageFrameDecoder(int maxFrameLength) {
        /**
         * 协议中，
         * 指定了length字段的偏移量是第12个字节，
         * length字段是int类型，占4个字节，
         * length之后就是正文，没有相对length偏移到正文的偏移量为0，
         * 需要保留整个报文，不需要去除报文头，去除0个字节
         */
        this(maxFrameLength, 12, 4, 0, 0);
    }

    private NettyPackageFrameDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }
}
