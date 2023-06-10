package i2f.liteflow.consts;

/**
 * @author Ice2Faith
 * @date 2023/6/7 11:16
 * @desc
 */
public enum LiteFlowNodeType {
    BEGIN(0),
    END(1),
    NORMAL(2),
    PARALLEL_BEGIN(3),
    PARALLEL_END(4),

    ;
    private int code;

    private LiteFlowNodeType(int code) {
        this.code = code;
    }

    public int code() {
        return this.code;
    }

    public static boolean isCode(Integer code) {
        if (code == null) {
            return false;
        }
        for (LiteFlowNodeType item : LiteFlowNodeType.values()) {
            if (item.code == code) {
                return true;
            }
        }
        return false;
    }
}
