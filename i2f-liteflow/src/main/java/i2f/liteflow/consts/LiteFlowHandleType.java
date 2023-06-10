package i2f.liteflow.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:16
 * @desc
 */
public enum LiteFlowHandleType {
    PERSON(0),
    ORGAN(1),

    ;
    private int code;

    private LiteFlowHandleType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    public static boolean isCode(Integer code) {
        if (code == null) {
            return false;
        }
        for (LiteFlowHandleType item : LiteFlowHandleType.values()) {
            if (item.code == code) {
                return true;
            }
        }
        return false;
    }
}
