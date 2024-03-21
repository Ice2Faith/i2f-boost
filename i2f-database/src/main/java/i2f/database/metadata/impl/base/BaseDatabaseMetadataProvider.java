package i2f.database.metadata.impl.base;

import i2f.database.jdbc.JdbcResolver;
import i2f.database.jdbc.data.BindSql;
import i2f.database.jdbc.data.QueryColumn;
import i2f.database.jdbc.data.QueryResult;
import i2f.database.jdbc.data.StdType;
import i2f.database.metadata.DatabaseMetadataProvider;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.mysql.MySqlType;
import i2f.database.metadata.impl.oracle.OracleType;
import i2f.database.metadata.impl.postgresql.PostgreSqlType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/15 9:41
 * @desc
 */
public abstract class BaseDatabaseMetadataProvider implements DatabaseMetadataProvider {

    public String extractTypeName(Map<String,Object> row){
        return asString(row.get("TYPE_NAME"),null);
    }

    @Override
    public List<String> getDataTypes(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getTypeInfo();
        QueryResult result = JdbcResolver.parseResultSet(rs);
        Set<String> ret=new LinkedHashSet<>();
        for (Map<String, Object> row : result.getRows()) {
            String str = extractTypeName(row);
            if(str!=null) {
                ret.add(str);
            }
        }
        return new ArrayList<>(ret);
    }

    public static String asString(Object val, String def){
        if(val instanceof String){
            return (String)val;
        }
        return asValue(val,(v)->String.valueOf(v),def);
    }

    public static Integer asInteger(Object val, Integer def){
        if(val instanceof Integer){
            return (Integer)val;
        }
        return asValue(val,(v)->new BigDecimal(String.valueOf(v)).intValue(),def);
    }

    public static Boolean asBoolean(Object val, Boolean def){
        if(val instanceof Boolean){
            return (Boolean)val;
        }
        return asValue(val,(v)->Boolean.parseBoolean(String.valueOf(v)),def);
    }

    public static Long asLong(Object val, Long def){
        if(val instanceof Long){
            return (Long)val;
        }
        return asValue(val,(v)->new BigDecimal(String.valueOf(v)).longValue(),def);
    }

    public static BigDecimal asBigDecimal(Object val, BigDecimal def){
        if(val instanceof BigDecimal){
            return (BigDecimal)val;
        }
        return asValue(val,(v)->new BigDecimal(String.valueOf(v)),def);
    }

    public String extractCatalog(Map<String,Object> row){
        return asString(row.get("TABLE_CAT"),null);
    }

    @Override
    public List<String> getCatalogs(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getCatalogs();
        QueryResult result = JdbcResolver.parseResultSet(rs);
        Set<String> ret=new LinkedHashSet<>();
        for (Map<String, Object> row : result.getRows()) {
            String str = extractCatalog(row);
            if(str!=null) {
                ret.add(str);
            }
        }

        return new ArrayList<>(ret);
    }

    public String extractSchema(Map<String,Object> row){
        return asString(row.get("TABLE_SCHEM"),null);
    }

    @Override
    public List<String> getSchemas(Connection conn) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = metaData.getSchemas();
        QueryResult result = JdbcResolver.parseResultSet(rs);
        Set<String> ret=new LinkedHashSet<>();
        for (Map<String, Object> row : result.getRows()) {
            String str = extractSchema(row);
            if(str!=null) {
                ret.add(str);
            }
        }

        return new ArrayList<>(ret);
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return getSchemas(conn);
    }

    public TableMeta parseTable(Map<String,Object> row){
        TableMeta table=new TableMeta();
        table.setCatalog(asString(row.get("TABLE_CAT"),null));
        table.setSchema(asString(row.get("TABLE_SCHEM"),null));
        table.setDatabase(table.getSchema());
        table.setName(asString(row.get("TABLE_NAME"),null));
        table.setComment(asString(row.get("REMARKS"),null));
        table.setType(asString(row.get("TABLE_TYPE"),null));

        return table;
    }

    public ResultSet getTables(DatabaseMetaData metaData,String database) throws SQLException {
        return metaData.getTables(null,database,  null, null);
    }

    @Override
    public List<TableMeta> getTables(Connection conn, String database) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = getTables(metaData,database);
        boolean notComment=true;
        QueryResult result = JdbcResolver.parseResultSet(rs);
        List<TableMeta> ret=new ArrayList<>();
        for (Map<String, Object> row : result.getRows()) {
            TableMeta table = parseTable(row);
            if(table.getDatabase()==null){
                table.setDatabase(database);
            }
            if(table.getComment()!=null){
                notComment=false;
            }
            ret.add(table);
        }
        if(notComment){
            Map<String, String> map = getTablesCommentMap(conn, database);

            for (TableMeta item : ret) {
                if(item.getComment()==null){
                    item.setComment(map.get(item.getName()));
                }
            }
        }
        return ret;
    }

    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        QueryResult result = new QueryResult();
        result.setColumns(new ArrayList<>());
        result.setRows(new LinkedList<>());
        return result;
    }

    public String extractTablesCommentTableName(Map<String,Object> row){
        return asString(row.get("TABLE_NAME"),null);
    }

    public String extractTablesCommentTableComment(Map<String,Object> row){
        return asString(row.get("TABLE_COMMENT"),null);
    }

    public Map<String,String> getTablesCommentMap(Connection conn,String database) throws SQLException {
        Map<String,String> map=new HashMap<>();
        QueryResult queryResult = getTablesComment(conn, database);
        for (Map<String, Object> row : queryResult.getRows()) {
            String name = extractTablesCommentTableName(row);
            String comment = extractTablesCommentTableComment(row);
            if(name!=null) {
                map.put(name, comment);
            }
        }
        return map;
    }

    public ResultSet getTableInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getTables( null,database, table, null);
    }

    public ResultSet getColumns(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getColumns( null,database, table, null);
    }

    public ResultSet getPrimaryKeys(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getPrimaryKeys( null,database, table);
    }

    public ResultSet getIndexInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getIndexInfo(null,database,table,false,false);
    }

    @Override
    public TableMeta getTableInfo(Connection conn, String database, String table) throws SQLException {
        DatabaseMetaData metaData = conn.getMetaData();
        ResultSet rs = getTableInfo(metaData,database,table);
        QueryResult result = JdbcResolver.parseResultSet(rs);
        List<Map<String, Object>> rows = result.getRows();
        if(rows.isEmpty()){
            return null;
        }
        TableMeta ret = parseTable(rows.get(0));
        if(ret.getDatabase()==null){
            ret.setDatabase(database);
        }
        if(ret.getComment()==null){
            Map<String, String> map = getTablesCommentMap(conn, ret.getDatabase());
            ret.setComment(map.get(ret.getName()));
        }

        rs = getColumns(metaData,ret.getDatabase(),ret.getName());
        result=JdbcResolver.parseResultSet(rs);

        List<ColumnMeta> columns=new ArrayList<>();
        ret.setColumns(columns);

        boolean noComment=true;
        for (Map<String, Object> row : result.getRows()) {
            ColumnMeta meta=parseColumn(row);
            columns.add(meta);
            if(meta.getComment()!=null){
                noComment=false;
            }
        }

        if(noComment){
            Map<String,String> map=getColumnsCommentMap(conn,ret.getDatabase(),ret.getName());
            for (ColumnMeta item : columns) {
                if(item.getComment()==null){
                    item.setComment(map.get(item.getName()));
                }
            }
        }

        parsePrimaryKey(metaData,ret);

        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        parseIndexes(metaData,ret);

        parseMoreTableInfo(conn,ret);

        ret.getColumns().sort((v1,v2)->Integer.compare(v1.getIndex(),v2.getIndex()));

        if(ret.getPrimary()!=null){
            ret.getPrimary().getColumns().sort((v1,v2)->Integer.compare(v1.getIndex(),v2.getIndex()));
        }
        if(ret.getUniqueIndexes()!=null){
            for (IndexMeta idx : ret.getUniqueIndexes()) {
                idx.getColumns().sort((v1,v2)->Integer.compare(v1.getIndex(),v2.getIndex()));
            }
        }
        if(ret.getIndexes()!=null){
            for (IndexMeta idx : ret.getIndexes()) {
                idx.getColumns().sort((v1,v2)->Integer.compare(v1.getIndex(),v2.getIndex()));
            }
        }


        return ret;
    }

    public void parseMoreTableInfo(Connection conn,TableMeta meta) throws SQLException {

    }

    public void parsePrimaryKey(DatabaseMetaData metaData,TableMeta ret) throws SQLException {
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

    public void parseIndexes(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs=getIndexInfo(metaData,ret.getDatabase(),ret.getName());
        QueryResult result=JdbcResolver.parseResultSet(rs);

        Map<String, IndexMeta> indexMap=new LinkedHashMap<>();
        for (Map<String, Object> row : result.getRows()) {
            String indexName= asString(row.get("INDEX_NAME"),null);
            if(indexName==null){
                continue;
            }
            if(!indexMap.containsKey(indexName)){
                IndexMeta meta=new IndexMeta();
                meta.setName(indexName);
                Boolean nonUnique = asBoolean(row.get("NON_UNIQUE"),null);
                if(nonUnique==null){
                    nonUnique=true;
                }
                meta.setUnique(!nonUnique);
                meta.setColumns(new ArrayList<>());
                indexMap.put(indexName,meta);
            }
            IndexColumnMeta cm=new IndexColumnMeta();
            cm.setName(asString(row.get("COLUMN_NAME"),null));
            cm.setIndex(asInteger(row.get("ORDINAL_POSITION"),0));
            cm.setDesc(!"A".equalsIgnoreCase(asString(row.get("ASC_OR_DESC"),null)));
            Integer type = asInteger(row.get("TYPE"),-1);
            cm.setType(getIndexType(type));
            indexMap.get(indexName).getColumns().add(cm);
        }

        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        for (Map.Entry<String, IndexMeta> entry : indexMap.entrySet()) {
            IndexMeta meta = entry.getValue();
            meta.getColumns().sort((v1,v2)->Integer.compare(v1.getIndex(),v2.getIndex()));
            if("PRIMARY".equalsIgnoreCase(meta.getName())){
                ret.setPrimary(meta);
            }else if(meta.isUnique()){
                ret.getUniqueIndexes().add(meta);
            }else{
                ret.getIndexes().add(meta);
            }
        }
    }

    public QueryResult getColumnsComment(Connection conn,String database,String table) throws SQLException {
        QueryResult result = new QueryResult();
        result.setColumns(new ArrayList<>());
        result.setRows(new LinkedList<>());
        return result;
    }

    public String extractColumnsCommentColumnName(Map<String,Object> row){
        return asString(row.get("COLUMN_NAME"),null);
    }

    public String extractColumnsCommentColumnComment(Map<String,Object> row){
        return asString(row.get("COLUMN_COMMENT"),null);
    }

    public Map<String, String> getColumnsCommentMap(Connection conn, String database, String table) throws SQLException {
        Map<String,String> map=new HashMap<>();
        QueryResult queryResult = getColumnsComment(conn, database,table);
        for (Map<String, Object> row : queryResult.getRows()) {
            String name = extractColumnsCommentColumnName(row);
            String comment = extractColumnsCommentColumnComment(row);
            map.put(name,comment);
        }
        return map;
    }


    public ColumnMeta parseColumn(Map<String,Object> row){
        ColumnMeta col=new ColumnMeta();
        col.setIndex(asInteger(row.get("ORDINAL_POSITION"),0));
        col.setName(asString(row.get("COLUMN_NAME"),null));
        col.setType(asString(row.get("TYPE_NAME"),null));
        if(col.getType()!=null){
            col.setType(col.getType().split("\\(",2)[0]);
        }
        col.setComment(asString(row.get("REMARKS"),null));
        col.setPrecision(asInteger(row.get("COLUMN_SIZE"),0));
        col.setScale(asInteger(row.get("DECIMAL_DIGITS"),0));
        col.setNullable(("YES".equals(asString(row.get("IS_NULLABLE"),null))));
        col.setAutoIncrement(("YES".equals(asString(row.get("IS_AUTOINCREMENT"),null))));
        col.setGenerated(("YES".equals(asString(row.get("IS_GENERATEDCOLUMN"),null))));
        col.setDefaultValue(asString(row.get("COLUMN_DEF"),null));

        StdType type=StdType.VARCHAR;
        for (StdType item : StdType.values()) {
            if(item.text().equalsIgnoreCase(col.getType())){
                type=item;
                break;
            }
        }

        String columnType=col.getType();
        if(col.getPrecision()>0){
            columnType+="("+col.getPrecision();
            if(col.getScale()>0){
                columnType+=","+col.getScale();
            }
            columnType+=")";
        }
        col.setColumnType(columnType);

        col.setJavaType(type.javaType().getSimpleName());
        col.setJdbcType(type.jdbcType().getName());
        col.setStdType(type.text());
        return col;
    }


    public static String getIndexType(int type){
        switch (type){
            case DatabaseMetaData.tableIndexStatistic:
                return "Statistic";
            case DatabaseMetaData.tableIndexClustered:
                return "Clustered";
            case DatabaseMetaData.tableIndexHashed:
                return "Hashed";
            case DatabaseMetaData.tableIndexOther:
                return "Other";
            default:
                break;
        }
        return "";
    }

    @Override
    public TableMeta getTableInfoByQuery(QueryResult result) throws SQLException {
        TableMeta ret=new TableMeta();
        ret.setColumns(new ArrayList<>());

        Map<String, StdType> typeMap = new HashMap<>();
        for (PostgreSqlType item : PostgreSqlType.values()) {
            typeMap.put(item.text(), item.stdType());
        }
        for (OracleType item : OracleType.values()) {
            typeMap.put(item.text(), item.stdType());
        }
        for (MySqlType item : MySqlType.values()) {
            typeMap.put(item.text(), item.stdType());
        }

        for (QueryColumn column : result.getColumns()) {
            ColumnMeta meta=new ColumnMeta();
            meta.setIndex(column.getIndex());
            meta.setName(column.getName());
            meta.setType(column.getTypeName());
            if (meta.getType() != null) {
                meta.setType(meta.getType().split("\\(", 2)[0]);
            }
            meta.setComment(null);

            meta.setPrecision(column.getPrecision());
            meta.setScale(column.getScale());

            meta.setNullable(column.isNullable());
            meta.setAutoIncrement(column.isAutoIncrement());
            meta.setGenerated(false);
            meta.setDefaultValue(null);



            StdType type = StdType.VARCHAR;
            for (Map.Entry<String, StdType> entry : typeMap.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(meta.getType())) {
                    type = entry.getValue();
                    break;
                }
            }

            String columnType = meta.getType();

            if (type.precision()) {
                columnType += "(" + meta.getPrecision();
                if (type.scale()) {
                    columnType += "," + meta.getScale();
                }
                columnType += ")";
            }

            meta.setColumnType(columnType);

            meta.setJavaType(type.javaType().getSimpleName());
            meta.setJdbcType(type.jdbcType().getName());
            meta.setStdType(type.text());
            meta.setLooseJavaType(type.looseJavaType().getSimpleName());
            meta.setLooseJdbcType(type.looseJdbcType().getName());

            ret.getColumns().add(meta);
        }

        return ret;
    }

    @Override
    public TableMeta getTableInfoByQuery(ResultSet rs) throws SQLException {
        QueryResult result = JdbcResolver.parseResultSet(rs);
        return getTableInfoByQuery(result);
    }

    @Override
    public TableMeta getTableInfoByQuery(Connection conn, String table) throws SQLException {
        if(table==null || "".equals(table)){
            throw new SQLException("bad table name found is : "+ table);
        }
        if(!table.matches("[a-zA-Z0-9\\_\\-`\"\\.\\$]+")){
            throw new SQLException("bad table name found is : "+ table);
        }
        String sql="select * from "+table + " where 1=2";
        QueryResult rs = JdbcResolver.query(conn, new BindSql(sql));
        TableMeta ret= getTableInfoByQuery(rs);
        String tableName=table;
        String schemaName=null;
        int idx=table.indexOf(".");
        if(idx>=0){
            tableName=table.substring(idx+1);
            schemaName=tableName.substring(0,idx);
        }
        if(tableName!=null){
            if(tableName.startsWith("`")
            || tableName.startsWith("\"")){
                tableName=tableName.substring(1,tableName.length()-1);
            }
        }
        if(schemaName!=null){
            if(schemaName.startsWith("`")
                    || schemaName.startsWith("\"")){
                schemaName=schemaName.substring(1,schemaName.length()-1);
            }
        }
        ret.setName(tableName);
        ret.setSchema(schemaName);
        ret.setCatalog(schemaName);
        ret.setType("TABLE");
        return ret;
    }

    @FunctionalInterface
    public interface TypeConverter<T,R> {
        R apply(T obj) throws Exception;
    }

    public static<T,R> R asValue(T val, TypeConverter<T,R> mapper, R def){
        if(val==null){
            return def;
        }
        try {
            return mapper.apply(val);
        }catch (Exception e){

        }
        return def;
    }
}
