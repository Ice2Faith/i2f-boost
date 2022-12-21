package i2f.extension.netty.tcp.tcp;

/**
 * 定义了最基本的启动接口
 * 基于连接的连接
 * 都需要启动
 * 作为TCP端的标准接口
 */
public interface INettyStarter {
    void start();
}
