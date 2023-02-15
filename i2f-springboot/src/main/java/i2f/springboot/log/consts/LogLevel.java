package i2f.springboot.log.consts;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:15
 * @desc
 */
public enum LogLevel {
    ERROR(0, "ERROR"),
    WARN(1, "WARN"),
    INFO(2, "INFO"),
    DEBUG(3, "DEBUG"),
    TRACE(4, "TRACE");

    private int code;
    private String text;

    LogLevel(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int code() {
        return code;
    }

    public String text() {
        return text;
    }
}
