package i2f.core.db.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class TableColumnMeta {
    private String name;
    private int type;
    private String typeName;
    private int displaySize;
    private String javaTypeString;
    private Class javaType;

    private String catalogName;
    private String label;

    private String tableName;
    private String schemaName;
    private int precision;
    private int scale;

    private String remark;

    private String dataType;
    private String columnSize;
    private String bufferLength;
    private String decimalDigits;
    private String numPrecRadix;
    private String nullable;
    private String columnDef;
    private String sqlDataType;
    private String charOctetLength;
    private String ordinalPosition;
    private String isNullable;
    private String sourceDataType;
    private String isAutoincrement;
    private String isGeneratedColumn;

    private String isPrimaryKey;
    private String primaryKeyName;

    private String isUnique;
    private String uniqueKeyName;

    private String isIndex;
    private String indexKeyName;
    private String indexType;
    private String indexUsing;
}
