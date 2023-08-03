package i2f.core.log.consts;

import i2f.std.dict.IDict;

/**
 * @author Ice2Faith
 * @date 2023/8/2 8:50
 * @desc
 */
public enum LogLevel implements IDict {
    OFF(0,"关闭"),
    FATAL(1,"严重"),
    ERROR(2,"错误"),
    WARN(3,"警告"),
    INFO(4,"信息"),
    DEBUG(5,"调试"),
    TRACE(6,"跟踪"),
    ALL(10,"全部")
    ;

    private int code;
    private String text;

    LogLevel(int code, String text) {
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
