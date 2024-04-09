package i2f.sql.test;

/**
 * @author Ice2Faith
 * @date 2024/4/9 14:14
 * @desc
 */
public class SysDict {
    private Long id;
    private String dictType;
    private Integer dictValue;
    private String dictText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Integer getDictValue() {
        return dictValue;
    }

    public void setDictValue(Integer dictValue) {
        this.dictValue = dictValue;
    }

    public String getDictText() {
        return dictText;
    }

    public void setDictText(String dictText) {
        this.dictText = dictText;
    }
}
