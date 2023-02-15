package i2f.springboot.log.consts;

/**
 * @author Ice2Faith
 * @date 2023/2/7 14:15
 * @desc
 */
public enum LogOperateType {
    QUERY(0, "查询"),
    ADD(1, "新增"),
    UPDATE(2, "修改"),
    DELETE(3, "删除"),
    REQUEST(4, "申请"),
    ADUIT(5, "审批"),
    IMPORT(6, "导入"),
    EXPORT(7, "导出");

    private int code;
    private String text;

    LogOperateType(int code, String text) {
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
