package i2f.extension.netty.tcp.tcp;

import i2f.extension.netty.tcp.protocol.consts.NettyConst;
import io.netty.handler.logging.LogLevel;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import lombok.Getter;
import lombok.Setter;

/**
 * 在TCP中，有很多属性时客户端和服务器通用的配置
 * 因此，这里就作为服务端和客户端的父类
 */
@Getter
@Setter
public abstract class NettyTcpEndPoint implements INettyStarter {
    protected InternalLogger logger = InternalLoggerFactory.getInstance(this.getClass());

    /**
     * 帧解码器的最大帧大小
     */
    protected int maxFrameLength = NettyConst.DEFAULT_MAX_FRAME_LENGTH;

    /**
     * 监听或连接的端口
     */
    protected int port = NettyConst.DEFAULT_NETTY_SERVER_PORT;

    /**
     * 日志级别，为空则不开启日志
     */
    protected LogLevel logLevel = null;

    /**
     * 是否启用协议的标志处理
     */
    protected boolean enablePkgFlag = NettyConst.DEFAULT_NETTY_FLAG_ENABLE;


    /**
     * 空闲时长
     */
    protected int idleTimeoutSeconds = NettyConst.DEFAULT_IDLE_TIMEOUT_SECOND;
    /**
     * 是否启用心跳机制
     */
    protected boolean enableHeartBeat = NettyConst.DEFAULT_NETTY_HEART_BEAT_ENABLE;

    /**
     * 是否具备目标日志等级需要输出日志
     *
     * @return
     */
    protected boolean isEnableLogging() {
        if (this.logLevel == null) {
            return false;
        }
        if (this.logger.isEnabled(this.logLevel.toInternalLevel())) {
            return true;
        }
        return false;
    }

}
