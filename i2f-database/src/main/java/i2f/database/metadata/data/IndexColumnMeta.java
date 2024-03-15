package i2f.database.metadata.data;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:45
 * @desc
 */
public class IndexColumnMeta {
    protected int index;
    protected String name;
    protected boolean isDesc;
    protected String type;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDesc() {
        return isDesc;
    }

    public void setDesc(boolean desc) {
        isDesc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
