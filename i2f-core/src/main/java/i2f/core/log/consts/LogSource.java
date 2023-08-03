package i2f.core.log.consts;

import i2f.std.dict.IDict;

/**
 * @author Ice2Faith
 * @date 2023/8/1 18:22
 * @desc
 */
public enum  LogSource implements IDict {
    AOP(0,"切面"),
    THROW(1,"异常"),
    OUTPUT(2,"输出"),
            ;

    private int code;
    private String text;

    LogSource(int code, String text) {
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
