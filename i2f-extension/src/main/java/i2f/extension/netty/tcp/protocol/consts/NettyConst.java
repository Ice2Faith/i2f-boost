package i2f.extension.netty.tcp.protocol.consts;

/**
 * 定义了协议中使用到的常量或者默认值
 */
public interface NettyConst {
    /**
     * 协议头的魔数
     */
    int MAGIC_NUMBER = 0xface1024;

    /**
     * 协议包的版本
     */
    byte PKG_VERSION = 1;

    /**
     * 是否启用flag处理标志
     */
    boolean DEFAULT_NETTY_FLAG_ENABLE = false;

    /**
     * 是否启用心跳检测
     */
    boolean DEFAULT_NETTY_HEART_BEAT_ENABLE = true;

    /**
     * 默认的netty端口
     */
    int DEFAULT_NETTY_SERVER_PORT = 1024;

    /**
     * 默认的最大帧大小（处理粘包、半包处理器参数）
     */
    int DEFAULT_MAX_FRAME_LENGTH = 64 * 1024;

    /**
     * 默认的心跳发送间隔秒数
     */
    int DEFAULT_IDLE_TIMEOUT_SECOND = 5 * 60;

}
