package i2f.annotations.doc.db;

import i2f.annotations.core.api.IDict;
import i2f.annotations.doc.base.Comment;

/**
 * @author Ice2Faith
 * @date 2024/2/21 14:53
 * @desc
 */
@Comment({
        "排序模式"
})
public enum OrderMode implements IDict {
    DEFAULT(0,"DEFAULT","默认",null),
    ASC(1,"ASC","升序",null),
    DESC(-1,"DESC","降序",null),
    ;

    private int code;
    private String key;
    private String text;
    private String remark;

    OrderMode(int code, String key, String text, String remark) {
        this.code = code;
        this.key = key;
        this.text = text;
        this.remark = remark;
    }


    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String key() {
        return this.key;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public String remark() {
        return this.remark;
    }
}
