package i2f.database.metadata.impl.mysql;

import i2f.database.jdbc.JdbcResolver;
import i2f.database.jdbc.data.QueryResult;
import i2f.database.jdbc.data.StdType;
import i2f.database.metadata.data.ColumnMeta;
import i2f.database.metadata.data.IndexColumnMeta;
import i2f.database.metadata.data.IndexMeta;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.base.BaseDatabaseMetadataProvider;

import java.sql.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:56
 * @desc
 */
public class MysqlDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {

    public static final String DRIVER_NAME="com.mysql.cj.jdbc.Driver";

    public static final String MAVEN_DEPENDENCY="<dependency>\n" +
            "            <groupId>mysql</groupId>\n" +
            "            <artifactId>mysql-connector-java</artifactId>\n" +
            "            <version>8.0.26</version>\n" +
            "        </dependency>";

    @Override
    public String extractTypeName(Map<String,Object> row){
        return asString(row.get("TYPE_NAME"),null);
    }

    @Override
    public String extractCatalog(Map<String,Object> row){
        return asString(row.get("TABLE_CAT"),null);
    }

    @Override
    public String extractSchema(Map<String,Object> row){
        return asString(row.get("TABLE_SCHEM"),null);
    }

    @Override
    public List<String> getDatabases(Connection conn) throws SQLException {
        return super.getCatalogs(conn);
    }

    @Override
    public TableMeta parseTable(Map<String,Object> row){
        TableMeta table=new TableMeta();
        table.setCatalog(asString(row.get("TABLE_CAT"),null));
        table.setSchema(asString(row.get("TABLE_SCHEM"),null));
        table.setDatabase(table.getCatalog());
        table.setName(asString(row.get("TABLE_NAME"),null));
        table.setComment(asString(row.get("REMARKS"),null));
        table.setType(asString(row.get("TABLE_TYPE"),null));

        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData,String database) throws SQLException {
        return metaData.getTables(database, null, null, null);
    }

    @Override
    public QueryResult getTablesComment(Connection conn, String database) throws SQLException {
        String sql="select TABLE_CATALOG,TABLE_SCHEMA,TABLE_NAME,TABLE_TYPE,TABLE_COMMENT \n" +
                "from information_schema.TABLES \n" +
                "where TABLE_SCHEMA  = ? ";
        QueryResult result = JdbcResolver.query(conn, sql, Arrays.asList(database));
        return result;
    }

    @Override
    public String extractTablesCommentTableName(Map<String,Object> row){
        return asString(row.get("TABLE_NAME"),null);
    }

    @Override
    public String extractTablesCommentTableComment(Map<String,Object> row){
        return asString(row.get("TABLE_COMMENT"),null);
    }

    @Override
    public ResultSet getTableInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getTables( database,null, table, null);
    }

    @Override
    public ResultSet getColumns(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getColumns( database,null, table, null);
    }

    @Override
    public ResultSet getPrimaryKeys(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getPrimaryKeys( database,null, table);
    }

    @Override
    public ResultSet getIndexInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getIndexInfo(database,null,table,false,false);
    }

    @Override
    public QueryResult getColumnsComment(Connection conn,String database,String table) throws SQLException {
        String sql="select TABLE_SCHEMA,TABLE_NAME,COLUMN_NAME,ORDINAL_POSITION,COLUMN_COMMENT, \n" +
                "COLUMN_DEFAULT,IS_NULLABLE, \n" +
                "DATA_TYPE,COLUMN_TYPE,COLUMN_KEY, \n" +
                "CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE \n" +
                "from information_schema.COLUMNS \n" +
                "where TABLE_SCHEMA  = ? \n" +
                "and TABLE_NAME = ? \n" +
                "order by ORDINAL_POSITION ";
        return JdbcResolver.query(conn, sql, Arrays.asList(database, table));
    }

    @Override
    public String extractColumnsCommentColumnName(Map<String,Object> row){
        return asString(row.get("COLUMN_NAME"),null);
    }

    @Override
    public String extractColumnsCommentColumnComment(Map<String,Object> row){
        return asString(row.get("COLUMN_COMMENT"),null);
    }

    @Override
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
        MySqlType mysqlType= null;
        for (MySqlType item : MySqlType.values()) {
            if(item.text().equalsIgnoreCase(col.getType())){
                type=item.stdType();
                mysqlType=item;
                break;
            }
        }

        String columnType=col.getType();
        if(mysqlType!=null){
            if(mysqlType.precision()){
                columnType+="("+col.getPrecision();
                if(mysqlType.scale()){
                    columnType+=","+col.getScale();
                }
                columnType+=")";
            }
        }else{
            if(type.precision()){
                columnType+="("+col.getPrecision();
                if(type.scale()){
                    columnType+=","+col.getScale();
                }
                columnType+=")";
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
    public void parsePrimaryKey(DatabaseMetaData metaData,TableMeta ret) throws SQLException {
        ResultSet rs = getPrimaryKeys(metaData,ret.getDatabase(),ret.getName());
        QueryResult result=JdbcResolver.parseResultSet(rs);
        IndexMeta primary=new IndexMeta();
        List<IndexColumnMeta> primaryColumns=new ArrayList<>();
        primary.setColumns(primaryColumns);
        for (Map<String, Object> row : result.getRows()) {
            if(primary.getName()==null){
                primary.setName(asString(row.get("PK_NAME"),null));
                primary.setUnique(true);
            }
            IndexColumnMeta meta=new IndexColumnMeta();
            meta.setDesc(false);
            meta.setIndex(asInteger(row.get("KEY_SEQ"),0));
            meta.setName(asString(row.get("COLUMN_NAME"),null));
        }
        ret.setPrimary(primary);
    }

    @Override
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
            if("PRIMARY".equalsIgnoreCase(meta.getName())){
                ret.setPrimary(meta);
            }else if(meta.isUnique()){
                ret.getUniqueIndexes().add(meta);
            }else{
                ret.getIndexes().add(meta);
            }
        }
    }

}
