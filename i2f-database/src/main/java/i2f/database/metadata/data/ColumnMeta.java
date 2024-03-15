package i2f.database.metadata.data;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:29
 * @desc
 */
public class ColumnMeta {
    protected int index;
    protected String name;
    protected String type;
    protected String comment;

    protected int precision;
    protected int scale;

    protected boolean isNullable;
    protected boolean isAutoIncrement;
    protected boolean isGenerated;

    protected String defaultValue;

    protected String columnType;
    protected String javaType;
    protected String jdbcType;
    protected String stdType;
    protected String looseJavaType;
    protected String looseJdbcType;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJdbcType() {
        return jdbcType;
    }

    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public boolean isGenerated() {
        return isGenerated;
    }

    public void setGenerated(boolean generated) {
        isGenerated = generated;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getStdType() {
        return stdType;
    }

    public void setStdType(String stdType) {
        this.stdType = stdType;
    }

    public String getLooseJavaType() {
        return looseJavaType;
    }

    public void setLooseJavaType(String looseJavaType) {
        this.looseJavaType = looseJavaType;
    }

    public String getLooseJdbcType() {
        return looseJdbcType;
    }

    public void setLooseJdbcType(String looseJdbcType) {
        this.looseJdbcType = looseJdbcType;
    }
}
