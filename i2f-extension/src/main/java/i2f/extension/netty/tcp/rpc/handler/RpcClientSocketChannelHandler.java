package i2f.extension.netty.tcp.rpc.handler;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import i2f.extension.netty.tcp.rpc.JacksonSerializer;
import i2f.extension.netty.tcp.rpc.data.NettyRpcResp;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Promise;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 提供了RPC中客户端接受RPC返回值的处理器
 * 原理是使用promise根据seqId来确认执行结果
 * 实现异步调用的结果能够同步返回
 * 由于ISocketChannelHandler是标准的定义接口，
 * 因此可以继承拓展此类，来实现一些附加的功能
 */
@Getter
public class RpcClientSocketChannelHandler implements ISocketChannelHandler {
    private Map<Integer, Promise<NettyRpcResp>> promises = new ConcurrentHashMap<>();
    private AtomicInteger seqId = new AtomicInteger(0);

    public synchronized int nextSeqId() {
        if (seqId.get() >= 0x07ffff) {
            seqId.set(0);
        }
        return seqId.getAndIncrement();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyPackage pkg = (NettyPackage) msg;
        int seqId = pkg.seqId;
        Promise<NettyRpcResp> promise = promises.get(seqId);
        promises.remove(seqId);
        try {
            String json = NettyPackages.parseUtf8(pkg);
            NettyRpcResp resp = JacksonSerializer.parseJson(json, NettyRpcResp.class);
            promise.setSuccess(resp);
        } catch (Throwable e) {
            promise.setFailure(e);
        }
    }
}
