package i2f.extension.netty.tcp.rpc.data;

import java.lang.reflect.Method;

/**
 * 定义了RPC通信的数据结构
 * remote process call
 * 这是适用于JAVA的rpc定义
 * 因此，在java中，需要调用一个远程方法
 * 最基本的信息都描述在这里
 * 本类提供了静态的构造方法，方便直接构造
 */
public class NettyRpcReq {
    public Class<?> clazz;
    public String method;
    public Class<?> returnType;
    public Class<?>[] parameters;
    public Object[] arguments;

    public static NettyRpcReq of(Method method, Object[] args) {
        NettyRpcReq req = new NettyRpcReq();
        req.clazz = method.getDeclaringClass();
        req.method = method.getName();
        req.returnType = method.getReturnType();
        req.parameters = method.getParameterTypes();
        req.arguments = args;
        return req;
    }
}
