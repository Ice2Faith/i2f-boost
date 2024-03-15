package i2f.database.metadata.data;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:29
 * @desc
 */
public class TableMeta {
    protected String catalog;
    protected String schema;

    protected String database;

    protected String name;
    protected String comment;
    protected String type;

    protected List<ColumnMeta> columns;

    protected IndexMeta primary;

    protected List<IndexMeta> uniqueIndexes;

    protected List<IndexMeta> indexes;

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<ColumnMeta> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnMeta> columns) {
        this.columns = columns;
    }

    public IndexMeta getPrimary() {
        return primary;
    }

    public void setPrimary(IndexMeta primary) {
        this.primary = primary;
    }

    public List<IndexMeta> getUniqueIndexes() {
        return uniqueIndexes;
    }

    public void setUniqueIndexes(List<IndexMeta> uniqueIndexes) {
        this.uniqueIndexes = uniqueIndexes;
    }

    public List<IndexMeta> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexMeta> indexes) {
        this.indexes = indexes;
    }
}
