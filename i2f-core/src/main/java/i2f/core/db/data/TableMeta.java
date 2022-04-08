package i2f.core.db.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class TableMeta {
    private String schema;
    private String table;
    private List<TableColumnMeta> columns=new ArrayList<>();

    private String catalog;
    private String type;
    private String remark;
    private String typeCatalog;
    private String typeSchema;
    private String typeName;
    private String selfReferencingColName;
    private String refGeneration;
}
