package i2f.extension.netty.tcp.protocol.consts;

/**
 * 定义了协议中的flag字段的含义
 * 分别是
 * echo 回拨，不管是客户端还是服务器，收到echo之后，会完整将收到的包直接返回，可以用于在线检测，测试连通性
 * broadcast 是对于客户端发送给服务器的，指定是否是一条广播消息，服务器将会广播给所有的客户端
 * heart beat 是心跳包，负责连接的保活，长时间没有收到心跳包，服务器将会关闭客户端的连接
 * custom 指示了，用户可以根据自己的需要定义其他的flag标志位，客户定义的标志位的值应该大于等于CUSTOM的code值
 */
public enum NettyFlag implements INettyEnum {
    NONE(0, "NONE"),
    ECHO(1, "ECHO"),
    BROADCAST(2, "BROADCAST"),
    HEART_BEAT(3, "HEART_BEAT"),
    CUSTOM(9, "CUSTOM");

    private int code;
    private String text;

    NettyFlag(int code, String text) {
        this.code = code;
        this.text = text;
    }


    @Override
    public int code() {
        return code;
    }

    @Override
    public String text() {
        return text;
    }
}
