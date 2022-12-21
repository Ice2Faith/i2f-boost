package i2f.extension.netty.tcp.rpc;

import i2f.extension.netty.tcp.handler.ISocketChannelHandler;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackage;
import i2f.extension.netty.tcp.protocol.pkg.NettyPackages;
import i2f.extension.netty.tcp.rpc.data.NettyRpcReq;
import i2f.extension.netty.tcp.rpc.data.NettyRpcResp;
import i2f.extension.netty.tcp.rpc.handler.RpcClientSocketChannelHandler;
import i2f.extension.netty.tcp.rpc.proxy.JdkRpcProxyHandler;
import i2f.extension.netty.tcp.tcp.NettyClient;
import io.netty.util.concurrent.DefaultPromise;
import lombok.Getter;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 提供了RPC的客户端
 * 客户端基于NettyClient
 * 只是对ISocketChannelHandler进行了RPC上的定制
 * 因此继承自NettyClient
 * 但是由于需要限制为RPC处理，因此私有化父类的构造参数
 * 采用实例方法instance进行初始化一个NettyRpcClient
 * 一般的使用场景中，用NettyRpcClient的proxy来代理一个RPC接口，
 * 直接使用RPC接口即可
 * 用法如下：
 * <p>
 * // 构造RPC客户端
 * NettyRpcClient client=NettyRpcClient.instance("localhost",9999);
 * <p>
 * // 设置连接参数
 * client.setEnableHeartBeat(true);
 * client.setLogLevel(LogLevel.DEBUG);
 * client.setEnablePkgFlag(false);
 * <p>
 * // 启动TCP连接
 * client.start();
 * <p>
 * // 代理指定的RPC接口
 * IRpcService service = client.proxy(IRpcService.class);
 * <p>
 * // 使用RPC接口
 * service.login(...)
 * ...
 */
@Getter
public class NettyRpcClient extends NettyClient {
    private static Map<String, NettyRpcClient> fastClientMap = new ConcurrentHashMap<>();

    private NettyRpcClient(String host, ISocketChannelHandler handler) {
        super(host, handler);
    }

    private NettyRpcClient(String host, int port, ISocketChannelHandler handler) {
        super(host, port, handler);
    }

    public static NettyRpcClient instance(String host) {
        String fastKey = host;
        if (fastClientMap.containsKey(fastKey)) {
            return fastClientMap.get(fastKey);
        }
        NettyRpcClient client = new NettyRpcClient(host, new RpcClientSocketChannelHandler());
        fastClientMap.put(fastKey, client);
        return client;
    }

    public static NettyRpcClient instance(String host, int port) {
        String fastKey = host + ":" + port;
        if (fastClientMap.containsKey(fastKey)) {
            return fastClientMap.get(fastKey);
        }
        NettyRpcClient client = new NettyRpcClient(host, port, new RpcClientSocketChannelHandler());
        fastClientMap.put(fastKey, client);
        return client;
    }

    public static NettyRpcClient instance(String host, int port, RpcClientSocketChannelHandler handler) {
        String fastKey = host + ":" + port;
        if (fastClientMap.containsKey(fastKey)) {
            return fastClientMap.get(fastKey);
        }
        NettyRpcClient client = new NettyRpcClient(host, port, handler);
        fastClientMap.put(fastKey, client);
        return client;
    }

    public static <T> T proxy(Class<T> inter, NettyRpcClient client) {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class[]{inter},
                new JdkRpcProxyHandler(client)
        );
    }

    public <T> T proxy(Class<T> inter) {
        return proxy(inter, this);
    }

    public <T> T rpc(Method method, Object[] args) {
        if (channel == null) {
            start();
        }
        try {
            NettyRpcReq req = NettyRpcReq.of(method, args);
            String json = JacksonSerializer.toJson(req);
            NettyPackage pkg = NettyPackages.ofUtf8(json);

            RpcClientSocketChannelHandler rpcHandler = (RpcClientSocketChannelHandler) handler;
            pkg.seqId = rpcHandler.nextSeqId();

            DefaultPromise<NettyRpcResp> promise = new DefaultPromise<>(channel.eventLoop());
            rpcHandler.getPromises().put(pkg.seqId, promise);

            this.getChannel().writeAndFlush(pkg);

            promise.await();
            if (promise.isSuccess()) {
                NettyRpcResp resp = promise.get();
                if (resp.success) {
                    return (T) resp.ret;
                } else {
                    throw new IllegalStateException("rcp response failure " + resp.exception + ":" + resp.message);
                }
            } else {
                throw promise.cause();
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new IllegalStateException("rcp call failure by " + e.getMessage(), e);
            }
        }
    }
}
