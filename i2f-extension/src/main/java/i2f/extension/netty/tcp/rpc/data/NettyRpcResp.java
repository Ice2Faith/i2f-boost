package i2f.extension.netty.tcp.rpc.data;

/**
 * 定义了RPC通信的返回值
 * 返回值有两种
 * 一种是正确的调用成功了，返回ret
 * 另一种是调用失败了，返回message和exception
 */
public class NettyRpcResp {
    public boolean success;

    public Object ret;

    public String message;
    public Class<? extends Throwable> exception;

    public static NettyRpcResp success(Object obj) {
        NettyRpcResp ret = new NettyRpcResp();
        ret.success = true;
        ret.ret = obj;
        ret.message = null;
        ret.exception = null;
        return ret;
    }

    public static NettyRpcResp error(Throwable thr) {
        NettyRpcResp ret = new NettyRpcResp();
        ret.success = false;
        ret.ret = null;
        ret.message = thr.getMessage();
        ret.exception = thr.getClass();
        return ret;
    }
}
