package i2f.extension.netty.tcp.protocol.consts;

/**
 * 指定了协议中的序列化类型
 * bin 二进制类型，是最具有通用性的类型
 * java JAVA的序列化类型，由JAVA提供
 * UTF8 是针对字符型的一种编码方式
 * GBK 是针对中文的一种编码方式
 * CUSTOM 指定了客户可拓展的序列化类型的最小值，客户定义的序列化类型应大于等于CUSTOM的code值
 * 预定义的类型，可以使用NettyPackages进行构造NettyPackage协议包
 */
public enum NettySerializeType implements INettyEnum {
    BIN(0, "BIN"),
    JAVA(1, "JAVA"),
    UTF8(2, "UTF8"),
    GBK(3, "GBK"),
    CUSTOM(10, "CUSTOM");

    private int code;
    private String text;

    NettySerializeType(int code, String text) {
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
