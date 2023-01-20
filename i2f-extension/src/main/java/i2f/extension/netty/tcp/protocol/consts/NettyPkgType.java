package i2f.extension.netty.tcp.protocol.consts;

/**
 * 定义了协议中的pkgType字段的含义
 * 分别是
 * none 无特殊含义
 * begin 连续包开始标志
 * part 连续包标志
 * end 连续包结束标志
*/
public enum NettyPkgType implements INettyEnum {
    NONE(0, "NONE"),
    BEGIN(1, "BEGIN"),
    PART(2, "PART"),
    END(3, "END"),
    CUSTOM(9, "CUSTOM");

    private int code;
    private String text;

    NettyPkgType(int code, String text) {
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
