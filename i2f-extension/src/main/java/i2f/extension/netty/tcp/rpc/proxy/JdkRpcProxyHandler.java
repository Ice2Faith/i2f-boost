package i2f.extension.netty.tcp.rpc.proxy;

import i2f.extension.netty.tcp.rpc.NettyRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 提供了接口的代理
 * 提供了对接口类的RPC调用代理
 * 在RPC中，需要做到对用于来说透明，不需要显示的使用RPC协议
 * 而是直接对方法进行使用
 * 因此使用接口代理RPC的细节即可
 */
public class JdkRpcProxyHandler implements InvocationHandler {
    private NettyRpcClient client;

    public JdkRpcProxyHandler(NettyRpcClient client) {
        this.client = client;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return client.rpc(method, args);
    }
}
