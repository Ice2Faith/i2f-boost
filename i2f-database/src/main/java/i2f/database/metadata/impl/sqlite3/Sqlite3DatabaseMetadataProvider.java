package i2f.database.metadata.impl.sqlite3;

import i2f.database.jdbc.JdbcResolver;
import i2f.database.jdbc.data.QueryResult;
import i2f.database.jdbc.data.StdType;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:56
 * @desc
 */
public class Sqlite3DatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final String DRIVER_NAME="org.sqlite.JDBC";

    public static final String MAVEN_DEPENDENCY="<dependency>\n" +
            "            <groupId>org.xerial</groupId>\n" +
            "            <artifactId>sqlite-jdbc</artifactId>\n" +
            "            <version>3.36.0.3</version>\n" +
            "        </dependency>";

    @Override
    public String extractTypeName(Map<String, Object> row) {
        return asString(row.get("TYPE_NAME"), null);
    }

    @Override
    public String extractCatalog(Map<String, Object> row) {
        return asString(row.get("TABLE_CAT"), null);
    }

    @Override
    public String extractSchema(Map<String, Object> row) {
        return asString(row.get("TABLE_SCHEM"), null);
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return super.getCatalogs(conn);
    }

    @Override
    public TableMeta parseTable(Map<String, Object> row) {
        TableMeta table = new TableMeta();
        table.setCatalog(asString(row.get("TABLE_CAT"), null));
        table.setSchema(asString(row.get("TABLE_SCHEM"), null));
        table.setDatabase(table.getCatalog());
        table.setName(asString(row.get("TABLE_NAME"), null));
        table.setComment(asString(row.get("REMARKS"), null));
        table.setType(asString(row.get("TABLE_TYPE"), null));

        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData, String database) throws SQLException {
        return metaData.getTables(database, null, null, null);
    }

    @Override
    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        QueryResult tables=querySqliteAllTable(conn);
        for (Map<String, Object> row : tables.getRows()) {
            TableMeta meta = parseSqliteDdl(conn, asString(row.get("name"), null));
            if(meta!=null){
                row.put("comment",meta.getComment());
            }
        }
        return tables;
    }

    @Override
    public String extractTablesCommentTableName(Map<String, Object> row) {
        return asString(row.get("name"), null);
    }

    @Override
    public String extractTablesCommentTableComment(Map<String, Object> row) {
        return asString(row.get("comment"), null);
    }

    @Override
    public ResultSet getTableInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getTables(database, null, table, null);
    }

    @Override
    public ResultSet getColumns(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getColumns(database, null, table, null);
    }

    @Override
    public ResultSet getPrimaryKeys(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getPrimaryKeys(database, null, table);
    }

    @Override
    public ResultSet getIndexInfo(DatabaseMetaData metaData, String database, String table) throws SQLException {
        return metaData.getIndexInfo(database, null, table, false, false);
    }

    @Override
    public QueryResult getColumnsComment(Connection conn, String database, String table) throws SQLException {
        String sql="select name,tbl_name as comment \n" +
                "from `sqlite_master` \n" +
                "where 1!=1 ";
        QueryResult result = JdbcResolver.query(conn, sql, null);
        TableMeta meta = parseSqliteDdl(conn, table);
        if(meta!=null){
            for (ColumnMeta column : meta.getColumns()) {
                Map<String,Object> elem=new LinkedHashMap<>();
                elem.put("name",column.getName());
                elem.put("comment",column.getComment());
                result.getRows().add(elem);
            }
        }
        return result;
    }

    @Override
    public String extractColumnsCommentColumnName(Map<String, Object> row) {
        return asString(row.get("name"), null);
    }

    @Override
    public String extractColumnsCommentColumnComment(Map<String, Object> row) {
        return asString(row.get("comment"), null);
    }

    @Override
    public ColumnMeta parseColumn(Map<String, Object> row) {
        ColumnMeta col = new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
        col.setName(asString(row.get("COLUMN_NAME"), null));
        col.setType(asString(row.get("TYPE_NAME"), null));
        if (col.getType() != null) {
            col.setType(col.getType().split("\\(", 2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"), null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"), 0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"), 0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"), null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"), null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"), null))));
        col.setDefaultValue(asString(row.get("COLUMN_DEF"), null));

        StdType type = StdType.VARCHAR;
        Sqlite3Type sqlite3Type = null;
        for (Sqlite3Type item : Sqlite3Type.values()) {
            if (item.text().equalsIgnoreCase(col.getType())) {
                type = item.stdType();
                sqlite3Type = item;
                break;
            }
        }

        String columnType = col.getType();
        if (sqlite3Type != null) {
            if (sqlite3Type.precision()) {
                columnType += "(" + col.getPrecision();
                if (sqlite3Type.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        } else {
            if (type.precision()) {
                columnType += "(" + col.getPrecision();
                if (type.scale()) {
                    columnType += "," + col.getScale();
                }
                columnType += ")";
            }
        }
        col.setColumnType(columnType);

        col.setJavaType(type.javaType().getSimpleName());
        col.setJdbcType(type.jdbcType().getName());
        col.setStdType(type.text());
        col.setLooseJavaType(type.looseJavaType().getSimpleName());
        col.setLooseJdbcType(type.looseJdbcType().getName());
        return col;
    }

    @Override
    public void parseMoreTableInfo(Connection conn, TableMeta meta) throws SQLException {
        TableMeta ddl = parseSqliteDdl(conn, meta.getName());
        if(ddl==null){
            return;
        }
        if(meta.getComment()==null || "".equals(meta.getComment())){
            meta.setComment(ddl.getComment());
        }
        if(meta.getPrimary()==null){
            meta.setPrimary(ddl.getPrimary());
        }
        if(meta.getUniqueIndexes()==null || meta.getUniqueIndexes().isEmpty()){
            if(ddl.getUniqueIndexes()!=null){
                meta.setUniqueIndexes(ddl.getUniqueIndexes());
            }
        }
        if(meta.getIndexes()==null || meta.getIndexes().isEmpty()){
            if(ddl.getIndexes()!=null){
                meta.setIndexes(ddl.getIndexes());
            }
        }
        if(meta.getColumns()==null || meta.getColumns().isEmpty()){
            if(ddl.getColumns()!=null){
                meta.setColumns(ddl.getColumns());
            }
        }
        if(meta.getColumns()==null){
            return;
        }
        Map<String,ColumnMeta> map=new LinkedHashMap<>();
        if(ddl.getColumns()==null){
            return;
        }
        for (ColumnMeta column : ddl.getColumns()) {
            map.put(column.getName(),column);
        }
        if(map.isEmpty()){
            return;
        }
        for (ColumnMeta column : meta.getColumns()) {
            ColumnMeta pdl = map.get(column.getName());
            if(pdl==null){
                continue;
            }
            if(column.getType()==null || "".equals(column.getType())){
                column.setType(pdl.getType());
                if(pdl.getPrecision()>0){
                    column.setPrecision(pdl.getPrecision());
                }
                if(pdl.getScale()>0){
                    column.setScale(pdl.getScale());
                }
                column.setColumnType(pdl.getColumnType());
                column.setJavaType(pdl.getJavaType());
                column.setJdbcType(pdl.getJdbcType());
                column.setStdType(pdl.getStdType());
                column.setLooseJavaType(pdl.getLooseJavaType());
                column.setLooseJdbcType(pdl.getLooseJdbcType());
            }
            if(column.getComment()==null || "".equals(column.getComment())){
                column.setComment(pdl.getComment());
            }
            if(!pdl.isNullable()){
                column.setNullable(pdl.isNullable());
            }
            if(pdl.isAutoIncrement()){
                column.setAutoIncrement(pdl.isAutoIncrement());
            }
            if(pdl.isGenerated()){
                column.setGenerated(pdl.isGenerated());
            }
        }
    }

    @Override
    public void parsePrimaryKey(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs = getPrimaryKeys(metaData,ret.getDatabase(),ret.getName());
        QueryResult result=JdbcResolver.parseResultSet(rs);
        IndexMeta primary=null;
        for (Map<String, Object> row : result.getRows()) {
            if(primary==null){
                primary=new IndexMeta();
                List<IndexColumnMeta> primaryColumns=new ArrayList<>();
                primary.setColumns(primaryColumns);
            }
            if(primary.getName()==null){
                primary.setName(asString(row.get("PK_NAME"),null));
                primary.setUnique(true);
            }
            IndexColumnMeta meta=new IndexColumnMeta();
            meta.setDesc(false);
            meta.setIndex(asInteger(row.get("KEY_SEQ"),0));
            meta.setName(asString(row.get("COLUMN_NAME"),null));
            primary.getColumns().add(meta);
        }
        ret.setPrimary(primary);
    }

    @Override
    public void parseIndexes(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs = getIndexInfo(metaData, ret.getDatabase(), ret.getName());
        QueryResult result = JdbcResolver.parseResultSet(rs);

        Map<String, IndexMeta> indexMap = new LinkedHashMap<>();
        for (Map<String, Object> row : result.getRows()) {
            String indexName = asString(row.get("INDEX_NAME"), null);
            if (indexName == null) {
                continue;
            }
            if (!indexMap.containsKey(indexName)) {
                IndexMeta meta = new IndexMeta();
                meta.setName(indexName);
                Boolean nonUnique = asInteger(row.get("NON_UNIQUE"), 0)==1;
                meta.setUnique(!nonUnique);
                meta.setColumns(new ArrayList<>());
                indexMap.put(indexName, meta);
            }
            IndexColumnMeta cm = new IndexColumnMeta();
            cm.setName(asString(row.get("COLUMN_NAME"), null));
            cm.setIndex(asInteger(row.get("ORDINAL_POSITION"), 0));
            cm.setDesc("1".equalsIgnoreCase(asString(row.get("ASC_OR_DESC"), null)));
            Integer type = asInteger(row.get("TYPE"), -1);
            cm.setType(getIndexType(type));
            indexMap.get(indexName).getColumns().add(cm);
        }

        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        for (Map.Entry<String, IndexMeta> entry : indexMap.entrySet()) {
            IndexMeta meta = entry.getValue();
            if ("PRIMARY".equalsIgnoreCase(meta.getName())) {
                ret.setPrimary(meta);
            } else if (meta.isUnique()) {
                ret.getUniqueIndexes().add(meta);
            } else {
                ret.getIndexes().add(meta);
            }
        }
    }

    public static QueryResult querySqliteAllTable(Connection conn) throws SQLException {
        String sql="select `type`,name,tbl_name,rootpage,'' as comment \n" +
                "from `sqlite_master` \n" +
                "where `type`= ? ";
        return JdbcResolver.query(conn,sql,Arrays.asList("table"));
    }

    public static TableMeta parseSqliteDdl(Connection conn, String table) throws SQLException {
        String sql = "select `type`,name,tbl_name,rootpage,`sql` \n" +
                "from `sqlite_master` \n" +
                "where `type`= ? \n" +
                "and name= ? ";
        QueryResult result = JdbcResolver.query(conn, sql, Arrays.asList("table", table));
        List<Map<String, Object>> rows = result.getRows();
        if (rows.isEmpty()) {
            return null;
        }

        String ddl = asString(rows.get(0).get("sql"), null);
        if (ddl == null) {
            return null;
        }

        TableMeta ret = new TableMeta();
        ret.setType("TABLE");
        ret.setColumns(new ArrayList<>());

        String[] lines = ddl.split("\n");
        for (String line : lines) {
            String[] arr = line.trim().split("\\s+");
            List<String> ls = new ArrayList<>();
            for (String item : arr) {
                String str = item;
                if (str.startsWith(",")) {
                    str = str.substring(1);
                }
                if (str.endsWith(",")) {
                    str = str.substring(0, str.length() - 1);
                }
                if ("".equals(str)) {
                    continue;
                }
                ls.add(str);
            }
            arr = ls.toArray(new String[0]);
            if (arr.length == 0) {
                continue;
            }
            if ("(".equalsIgnoreCase(arr[0])) {
                if (arr.length == 1) {
                    continue;
                }
                arr = Arrays.copyOf(arr, arr.length - 1);
            }
            if (")".equalsIgnoreCase(arr[0])) {
                if (arr.length == 1) {
                    continue;
                }
                for (int i = 1; i < arr.length; i++) {
                    if ("--".equalsIgnoreCase(arr[i])) {
                        String comment = "";
                        for (int j = i + 1; j < arr.length; j++) {
                            comment += " " + arr[j];
                        }
                        if (!comment.isEmpty()) {
                            comment = comment.substring(1);
                        }
                        ret.setComment(comment);
                        break;
                    }
                }
                break;
            }
            if ("create".equalsIgnoreCase(arr[0])) {
                // 表头
                for (int i = 0; i < arr.length; i++) {
                    if ("table".equalsIgnoreCase(arr[i])) {
                        ret.setName(arr[i + 1]);
                    }
                    if ("--".equalsIgnoreCase(arr[i])) {
                        String comment = "";
                        for (int j = i + 1; j < arr.length; j++) {
                            comment += " " + arr[j];
                        }
                        if (!comment.isEmpty()) {
                            comment = comment.substring(1);
                        }
                        ret.setComment(comment);
                        break;
                    }
                }
                continue;
            }
            ColumnMeta column = new ColumnMeta();
            column.setPrecision(0);
            column.setScale(0);
            column.setIndex(ret.getColumns().size());
            for (int i = 0; i < arr.length; i++) {
                if (i == 0) {
                    column.setName(arr[i]);
                }
                if (i == 1) {
                    String type=arr[i];
                    if(type.contains("(")){
                        String[] strs = type.split("\\(", 2);
                        type=strs[0];
                        String len = strs[1];
                        if(len.endsWith(")")){
                            len=len.substring(0,len.length()-1);
                        }
                        strs = len.split(",", 2);
                        if(strs.length>0){
                            column.setPrecision(asInteger(strs[0],0));
                        }
                        if(strs.length>1){
                            column.setScale(asInteger(strs[1],0));
                        }
                    }
                    column.setType(type);
                }
                if ("primary".equalsIgnoreCase(arr[i])) {
                    IndexMeta primary = new IndexMeta();
                    primary.setName("PRIMARY");
                    primary.setUnique(true);
                    IndexColumnMeta indexColumn = new IndexColumnMeta();
                    indexColumn.setName(column.getName());
                    indexColumn.setType("Hashed");
                    indexColumn.setDesc(false);
                    indexColumn.setIndex(0);
                    List<IndexColumnMeta> indexColumns = new ArrayList<>();
                    indexColumns.add(indexColumn);
                    primary.setColumns(indexColumns);
                    ret.setPrimary(primary);
                    column.setNullable(false);
                }
                if ("autoincrement".equalsIgnoreCase(arr[i])) {
                    column.setAutoIncrement(true);
                }
                if ("null".equalsIgnoreCase(arr[i])) {
                    column.setNullable(false);
                }
                if ("unique".equalsIgnoreCase(arr[i])) {
                    IndexMeta index = new IndexMeta();
                    index.setName("unq_" + column.getName());
                    index.setUnique(true);
                    IndexColumnMeta indexColumn = new IndexColumnMeta();
                    indexColumn.setName(column.getName());
                    indexColumn.setType("Hashed");
                    indexColumn.setDesc(false);
                    indexColumn.setIndex(0);
                    List<IndexColumnMeta> indexColumns = new ArrayList<>();
                    indexColumns.add(indexColumn);
                    index.setColumns(indexColumns);
                    if (ret.getUniqueIndexes() == null) {
                        ret.setUniqueIndexes(new ArrayList<>());
                    }
                    ret.getUniqueIndexes().add(index);
                }
                if ("--".equalsIgnoreCase(arr[i])) {
                    String comment = "";
                    for (int j = i + 1; j < arr.length; j++) {
                        comment += " " + arr[j];
                    }
                    if (!comment.isEmpty()) {
                        comment = comment.substring(1);
                    }
                    column.setComment(comment);
                    break;
                }
            }

            StdType type = StdType.VARCHAR;
            Sqlite3Type sqlite3Type = null;
            for (Sqlite3Type item : Sqlite3Type.values()) {
                if (item.text().equalsIgnoreCase(column.getType())) {
                    type = item.stdType();
                    sqlite3Type = item;
                    break;
                }
            }

            String columnType = column.getType();
            if (sqlite3Type != null) {
                if (sqlite3Type.precision()) {
                    columnType += "(" + column.getPrecision();
                    if (sqlite3Type.scale()) {
                        columnType += "," + column.getScale();
                    }
                    columnType += ")";
                }
            } else {
                if (type.precision()) {
                    columnType += "(" + column.getPrecision();
                    if (type.scale()) {
                        columnType += "," + column.getScale();
                    }
                    columnType += ")";
                }
            }
            column.setColumnType(columnType);

            column.setJavaType(type.javaType().getSimpleName());
            column.setJdbcType(type.jdbcType().getName());
            column.setStdType(type.text());
            column.setLooseJavaType(type.looseJavaType().getSimpleName());
            column.setLooseJdbcType(type.looseJdbcType().getName());
            ret.getColumns().add(column);
        }
        return ret;
    }

}
