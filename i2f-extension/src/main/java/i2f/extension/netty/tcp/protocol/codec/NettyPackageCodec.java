package i2f.extension.netty.tcp.protocol.codec;

import i2f.extension.netty.tcp.protocol.consts.NettyConst;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.List;

/**
 * 协议包
 * 编解码器
 * 负责将数据包编解码为协议包
 */
@ChannelHandler.Sharable
public class NettyPackageCodec extends MessageToMessageCodec<ByteBuf, NettyPackage> {

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyPackage pkg, List<Object> list) throws Exception {
        ByteBuf buf = ctx.alloc().buffer();
        buf.writeInt(pkg.magicNumber);
        buf.writeByte(pkg.version);
        buf.writeByte(pkg.serializeType);
        buf.writeByte(pkg.pkgType);
        buf.writeByte(pkg.flag);
        buf.writeInt(pkg.seqId);
        buf.writeInt(pkg.length);
        if (pkg.length > 0) {
            buf.writeBytes(pkg.content);
        }
        list.add(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> list) throws Exception {
        NettyPackage pkg = new NettyPackage();
        pkg.magicNumber = buf.readInt();
        if (NettyConst.MAGIC_NUMBER != pkg.magicNumber) {
            throw new IllegalStateException("bad netty package of wrong magic number :" + String.format("0x%08x", pkg.magicNumber));
        }
        pkg.version = buf.readByte();
        pkg.serializeType = buf.readByte();
        pkg.pkgType = buf.readByte();
        pkg.flag = buf.readByte();
        pkg.seqId = buf.readInt();
        pkg.length = buf.readInt();
        pkg.content = new byte[pkg.length];
        if (pkg.length > 0) {
            buf.readBytes(pkg.content, 0, pkg.length);
        }
        list.add(pkg);
    }
}
