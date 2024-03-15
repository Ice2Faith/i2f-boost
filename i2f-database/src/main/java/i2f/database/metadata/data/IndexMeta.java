package i2f.database.metadata.data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:44
 * @desc
 */
public class IndexMeta {
    protected boolean isUnique;
    protected String name;
    protected List<IndexColumnMeta> columns;

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IndexColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<IndexColumnMeta> columns) {
        this.columns = columns;
    }
}
