package i2f.core.verifycode.consts;

import i2f.std.dict.IDict;

/**
 * @author Ice2Faith
 * @date 2023/8/17 17:04
 * @desc
 */
public enum VerifyCodeType implements IDict {
    INPUT(0, "输入型"),
    D1(1, "一维"),
    D2(2, "二维"),
    D1_MULTI(3, "一维多点"),
    D2_MULTI(4, "二维多点");

    private int code;
    private String text;

    VerifyCodeType(int code, String text) {
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
