package i2f.springboot.limit.consts;

/**
 * @author Ice2Faith
 * @date 2023/8/3 8:46
 * @desc
 */
public enum LimitType {
    GLOBAL(0,"全局"),
    IP(1,"IP"),
    USER(2,"用户")
    ;

    private int code;
    private String text;

    LimitType(int code, String text) {
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
