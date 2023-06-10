package i2f.liteflow.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:16
 * @desc
 */
public enum LiteFlowInstanceStatus {
    STOP(0),
    RUN(1),
    FINISH(2),
    ;
    private int code;

    private LiteFlowInstanceStatus(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    public static boolean isCode(Integer code) {
        if (code == null) {
            return false;
        }
        for (LiteFlowInstanceStatus item : LiteFlowInstanceStatus.values()) {
            if (item.code == code) {
                return true;
            }
        }
        return false;
    }
}
