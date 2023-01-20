package i2f.extension.netty.tcp.protocol.pkg;

import lombok.ToString;

import java.io.InputStream;

/**
 * 定义了标准的通讯协议包
 */
@ToString
public class NettyMessage {
    public NettyPackage template;
    public InputStream is;
}
