package i2f.extension.netty.tcp.rpc.handler;

import i2f.core.functional.supplier.ISupplier1;
import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import i2f.extension.netty.tcp.rpc.JacksonSerializer;
import i2f.extension.netty.tcp.rpc.data.NettyRpcReq;
import i2f.extension.netty.tcp.rpc.data.NettyRpcResp;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 提供了RPC中服务端的处理器
 * 服务端主要是根据客户端指定的实现类
 * 查找响应的实现方法
 * 进行调用并返回
 * 同样也会ISocketChannelHandler接口的一个实现
 * 因此可以拓展其他的实现部分，进行额外的增强
 */
public class RpcServerSocketChannelHandler implements ISocketChannelHandler {
    private ISupplier1<Object, Class> beanSupplier;

    public RpcServerSocketChannelHandler(ISupplier1<Object, Class> beanSupplier) {
        this.beanSupplier = beanSupplier;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyPackage pkg = (NettyPackage) msg;
        NettyRpcResp resp = null;
        int seqId = pkg.seqId;
        try {

            String json = NettyPackages.parseUtf8(pkg);
            NettyRpcReq req = JacksonSerializer.parseJson(json, NettyRpcReq.class);
            Class<?> clazz = req.clazz;
            Method method = clazz.getMethod(req.method, req.parameters);
            Object ivkObj = beanSupplier.get(clazz);
            Object retObj = method.invoke(ivkObj, req.arguments);
            resp = NettyRpcResp.success(retObj);
        } catch (Throwable e) {
            if (e instanceof InvocationTargetException) {
                if (e.getCause() != null) {
                    e = e.getCause();
                }
            }
            resp = NettyRpcResp.error(e);
            e.printStackTrace();
        }
        String json = JacksonSerializer.toJson(resp);
        NettyPackage retPkg = NettyPackages.ofUtf8(json);
        retPkg.seqId = seqId;
        ctx.writeAndFlush(retPkg);
    }
}
