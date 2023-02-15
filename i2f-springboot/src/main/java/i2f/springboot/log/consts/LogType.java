package i2f.springboot.log.consts;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:15
 * @desc
 */
public enum LogType {
    LOGIN(0, "登录日志"),
    LOGOUT(1, "登出日志"),
    REGISTRY(2, "注册日志"),
    CANCEL(3, "注销日志"),
    API(4, "接口日志"),
    OUTPUT(5, "输出日志"),
    STATUS(6, "服务器状态"),
    EXCEPTION(7, "服务异常"),
    CALL(8, "调用日志"),
    CALLBACK(9, "回调日志");

    private int code;
    private String text;

    LogType(int code, String text) {
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
