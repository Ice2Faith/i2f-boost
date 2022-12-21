package i2f.extension.netty.tcp.protocol.pkg;

import lombok.ToString;

/**
 * 定义了标准的通讯协议包
 */
@ToString
public class NettyPackage {
    /**
     * 魔数
     */
    public int magicNumber; // 4
    /**
     * 版本
     */
    public byte version; // 1
    /**
     * 序列化类型
     */
    public byte serializeType; // 1
    /**
     * 包类型
     */
    public byte pkgType; // 1
    /**
     * 子类型
     */
    public byte flag; // 1
    /**
     * 请求号
     */
    public int seqId; // 4
    /**
     * 长度
     */
    public int length; // 4
    /**
     * 内容
     */
    public byte[] content;
}
