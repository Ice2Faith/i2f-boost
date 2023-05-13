package i2f.core.db.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

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
    private List<TableColumnMeta> columns = new ArrayList<>();

    private String catalog;
    private String type;
    private String remark;
    private String typeCatalog;
    private String typeSchema;
    private String typeName;
    private String selfReferencingColName;
    private String refGeneration;

    private List<TableIndexMeta> indexes = new ArrayList<>();


    public static class ColumnNameComparator implements Comparator<TableColumnMeta> {
        @Override
        public int compare(TableColumnMeta o1, TableColumnMeta o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public TableMeta sortColumns() {
        if (columns == null) {
            return this;
        }
        Collections.sort(columns, new ColumnNameComparator());
        return this;
    }

    public TableMeta inflateIndexes() {
        if (columns == null) {
            return this;
        }
        sortColumns();
        indexes = new ArrayList<>();
        Map<String, List<String>> primaryMap = new HashMap<>();
        Map<String, List<String>> uniqueMap = new HashMap<>();
        Map<String, List<String>> indexesMap = new HashMap<>();
        for (TableColumnMeta column : columns) {
            if ("YES".equals(column.getIsPrimaryKey())) {
                String name = column.getPrimaryKeyName();
                if (!primaryMap.containsKey(name)) {
                    primaryMap.put(name, new ArrayList<>());
                }
                primaryMap.get(name).add(column.getName());
            }
            if ("YES".equals(column.getIsUnique())) {
                String name = column.getUniqueKeyName();
                if (!uniqueMap.containsKey(name)) {
                    uniqueMap.put(name, new ArrayList<>());
                }
                uniqueMap.get(name).add(column.getName());
            }
            if ("YES".equals(column.getIsIndex())) {
                String name = column.getIndexKeyName();
                if (!indexesMap.containsKey(name)) {
                    indexesMap.put(name, new ArrayList<>());
                }
                indexesMap.get(name).add(column.getName());
            }
        }
        for (Map.Entry<String, List<String>> entry : primaryMap.entrySet()) {
            TableIndexMeta item = new TableIndexMeta();
            item.setType("primary");
            item.setName(entry.getKey());
            item.setColumns(entry.getValue());
        }
        for (Map.Entry<String, List<String>> entry : uniqueMap.entrySet()) {
            TableIndexMeta item = new TableIndexMeta();
            item.setType("unique");
            item.setName(entry.getKey());
            item.setColumns(entry.getValue());
        }
        for (Map.Entry<String, List<String>> entry : indexesMap.entrySet()) {
            TableIndexMeta item = new TableIndexMeta();
            item.setType("index");
            item.setName(entry.getKey());
            item.setColumns(entry.getValue());
        }
        return this;
    }
}
