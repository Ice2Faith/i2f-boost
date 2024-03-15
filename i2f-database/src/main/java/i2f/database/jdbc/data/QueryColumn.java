package i2f.database.jdbc.data;

import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/14 11:05
 * @desc
 */
public class QueryColumn {
    protected int index;
    protected String name;
    protected String catalog;
    protected String clazzName;
    protected int displaySize;
    protected String label;
    protected int type;
    protected String typeName;
    protected int precision;
    protected int scale;
    protected String schema;
    protected String table;
    protected boolean nullable;
    protected boolean autoIncrement;
    protected boolean readonly;
    protected boolean writable;
    protected boolean caseSensitive;
    protected boolean currency;
    protected boolean definitelyWritable;
    protected boolean searchable;
    protected boolean signed;

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

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public int getDisplaySize() {
        return displaySize;
    }

    public void setDisplaySize(int displaySize) {
        this.displaySize = displaySize;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    public boolean isWritable() {
        return writable;
    }

    public void setWritable(boolean writable) {
        this.writable = writable;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public boolean isCurrency() {
        return currency;
    }

    public void setCurrency(boolean currency) {
        this.currency = currency;
    }

    public boolean isDefinitelyWritable() {
        return definitelyWritable;
    }

    public void setDefinitelyWritable(boolean definitelyWritable) {
        this.definitelyWritable = definitelyWritable;
    }

    public boolean isSearchable() {
        return searchable;
    }

    public void setSearchable(boolean searchable) {
        this.searchable = searchable;
    }

    public boolean isSigned() {
        return signed;
    }

    public void setSigned(boolean signed) {
        this.signed = signed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryColumn column = (QueryColumn) o;
        return index == column.index &&
                displaySize == column.displaySize &&
                type == column.type &&
                precision == column.precision &&
                scale == column.scale &&
                nullable == column.nullable &&
                autoIncrement == column.autoIncrement &&
                readonly == column.readonly &&
                writable == column.writable &&
                caseSensitive == column.caseSensitive &&
                currency == column.currency &&
                definitelyWritable == column.definitelyWritable &&
                searchable == column.searchable &&
                signed == column.signed &&
                Objects.equals(name, column.name) &&
                Objects.equals(catalog, column.catalog) &&
                Objects.equals(clazzName, column.clazzName) &&
                Objects.equals(label, column.label) &&
                Objects.equals(typeName, column.typeName) &&
                Objects.equals(schema, column.schema) &&
                Objects.equals(table, column.table);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, name, catalog, clazzName, displaySize, label, type, typeName, precision, scale, schema, table, nullable, autoIncrement, readonly, writable, caseSensitive, currency, definitelyWritable, searchable, signed);
    }

    @Override
    public String toString() {
        return "QueryColumn{" +
                "index=" + index +
                ", name='" + name + '\'' +
                ", catalog='" + catalog + '\'' +
                ", clazzName='" + clazzName + '\'' +
                ", displaySize=" + displaySize +
                ", label='" + label + '\'' +
                ", type=" + type +
                ", typeName='" + typeName + '\'' +
                ", precision=" + precision +
                ", scale=" + scale +
                ", schema='" + schema + '\'' +
                ", table='" + table + '\'' +
                ", nullable=" + nullable +
                ", autoIncrement=" + autoIncrement +
                ", readonly=" + readonly +
                ", writable=" + writable +
                ", caseSensitive=" + caseSensitive +
                ", currency=" + currency +
                ", definitelyWritable=" + definitelyWritable +
                ", searchable=" + searchable +
                ", signed=" + signed +
                '}';
    }
}
