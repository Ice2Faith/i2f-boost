package i2f.core.db.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

    public static class ColumnNameComparator implements Comparator<TableColumnMeta>{
        @Override
        public int compare(TableColumnMeta o1, TableColumnMeta o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public TableMeta sortColumns(){
        if(columns==null){
            return this;
        }
        Collections.sort(columns,new ColumnNameComparator());
        return this;
    }
}
