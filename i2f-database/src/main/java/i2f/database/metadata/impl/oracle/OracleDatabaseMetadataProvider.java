package i2f.database.metadata.impl.oracle;

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
public class OracleDatabaseMetadataProvider extends BaseDatabaseMetadataProvider {
    public static final String DRIVER_NAME="oracle.jdbc.OracleDriver";

    public static final String MAVEN_DEPENDENCY="<dependency>\n" +
            "            <groupId>com.oracle</groupId>\n" +
            "            <artifactId>ojdbc8</artifactId>\n" +
            "            <version>12.2.0.1</version>\n" +
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
        return super.getSchemas(conn);
    }

    @Override
    public TableMeta parseTable(Map<String,Object> row){
        TableMeta table=new TableMeta();
        table.setCatalog((String)row.get("TABLE_CAT"));
        table.setSchema((String)row.get("TABLE_SCHEM"));
        table.setDatabase(table.getSchema());
        table.setName((String)row.get("TABLE_NAME"));
        table.setComment((String)row.get("REMARKS"));
        table.setType((String)row.get("TABLE_TYPE"));

        return table;
    }

    @Override
    public ResultSet getTables(DatabaseMetaData metaData,String database) throws SQLException {
        return metaData.getTables(null, database, null, null);
    }

    @Override
    public QueryResult getTablesComment(Connection conn,String database) throws SQLException {
        String sql="SELECT OWNER,TABLE_NAME,TABLE_TYPE,COMMENTS \n" +
                "\tFROM ALL_TAB_COMMENTS \n" +
                "\tWHERE OWNER= ? ";
        QueryResult result = JdbcResolver.query(conn, sql, Arrays.asList(database));
        return result;
    }

    @Override
    public String extractTablesCommentTableName(Map<String,Object> row){
        return asString(row.get("TABLE_NAME"),null);
    }

    @Override
    public String extractTablesCommentTableComment(Map<String,Object> row){
        return asString(row.get("COMMENTS"),null);
    }

    @Override
    public ResultSet getTableInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getTables( null,database, table, null);
    }

    @Override
    public ResultSet getColumns(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getColumns( null,database, table, null);
    }

    @Override
    public ResultSet getPrimaryKeys(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getPrimaryKeys( null,database, table);
    }

    @Override
    public ResultSet getIndexInfo(DatabaseMetaData metaData,String database,String table) throws SQLException {
        return metaData.getIndexInfo(null,database,table,false,false);
    }


    @Override
    public QueryResult getColumnsComment(Connection conn,String database,String table) throws SQLException {
        String sql="SELECT OWNER,TABLE_NAME,COLUMN_NAME,COMMENTS \n" +
                "\tFROM ALL_COL_COMMENTS \n" +
                "\tWHERE OWNER= ? \n" +
                "\tAND TABLE_NAME= ? ";
        return JdbcResolver.query(conn, sql, Arrays.asList(database, table));
    }

    @Override
    public String extractColumnsCommentColumnName(Map<String,Object> row){
        return asString(row.get("COLUMN_NAME"),null);
    }

    @Override
    public String extractColumnsCommentColumnComment(Map<String,Object> row){
        return asString(row.get("COMMENTS"),null);
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
        OracleType oracleType= null;
        for (OracleType item : OracleType.values()) {
            if(item.text().equalsIgnoreCase(col.getType())){
                type=item.stdType();
                oracleType=item;
                break;
            }
        }

        if(oracleType==OracleType.NUMBER){
            if(col.getScale()<=0){
                int prec = col.getPrecision();
                if(prec<=3){
                    type=StdType.TINYINT;
                }else if(prec<=5){
                    type=StdType.SMALLINT;
                }else if(prec<=10){
                    type=StdType.INT;
                }else if(prec<=20){
                    type=StdType.BIGINT;
                }
            }
        }

        String columnType=col.getType();
        if(oracleType!=null){
            if(oracleType.precision()){
                columnType+="("+col.getPrecision();
                if(oracleType.scale()){
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
//        ResultSet rs = getPrimaryKeys(metaData,ret.getDatabase(),ret.getName());
//        QueryResult result=JdbcResolver.parseResultSet(rs);
//        IndexMeta primary=new IndexMeta();
//        List<IndexColumnMeta> primaryColumns=new ArrayList<>();
//        primary.setColumns(primaryColumns);
//        for (Map<String, Object> row : result.getRows()) {
//            if(primary.getName()==null){
//                primary.setName(parseString(row.get("PK_NAME"),null));
//                primary.setUnique(true);
//            }
//            IndexColumnMeta meta=new IndexColumnMeta();
//            meta.setDesc(false);
//            meta.setIndex(parseInteger(row.get("KEY_SEQ"),0));
//            meta.setName(parseString(row.get("COLUMN_NAME"),null));
//        }
//        ret.setPrimary(primary);
    }


    @Override
    public void parseIndexes(DatabaseMetaData metaData, TableMeta ret) throws SQLException {
        ResultSet rs=getIndexInfo(metaData,ret.getDatabase(),ret.getName());
        QueryResult result=JdbcResolver.parseResultSet(rs);


        String primaryCardinality=null;
        IndexMeta primary=null;
        Map<String,IndexMeta> indexMap=new LinkedHashMap<>();
        for (Map<String, Object> row : result.getRows()) {
            String indexName= asString(row.get("INDEX_NAME"),null);
            if(indexName==null){
                primaryCardinality= asString(row.get("CARDINALITY"),null);
                continue;
            }
            if(!indexMap.containsKey(indexName)){
                IndexMeta meta=new IndexMeta();
                meta.setName(indexName);
                meta.setUnique("0".equals(asString(row.get("NON_UNIQUE"),null)));
                meta.setColumns(new ArrayList<>());

                String cardinality= asString(row.get("CARDINALITY"),null);
                if(cardinality.equals(primaryCardinality)){
                    primary=meta;
                }
                indexMap.put(indexName,meta);
            }
            IndexColumnMeta cm=new IndexColumnMeta();
            cm.setName(asString(row.get("COLUMN_NAME"),null));
            cm.setIndex(asInteger(row.get("ORDINAL_POSITION"),0));
            cm.setDesc("1".equalsIgnoreCase(asString(row.get("ASC_OR_DESC"),null)));

            Integer type = asInteger(row.get("TYPE"),-1);
            cm.setType(getIndexType(type));

            indexMap.get(indexName).getColumns().add(cm);
        }

        ret.setPrimary(primary);
        ret.setUniqueIndexes(new ArrayList<>());
        ret.setIndexes(new ArrayList<>());
        for (Map.Entry<String, IndexMeta> entry : indexMap.entrySet()) {
            IndexMeta meta = entry.getValue();
            if(primary!=null && meta.getName().equalsIgnoreCase(primary.getName())){
                continue;
            }
            if(meta.isUnique()){
                ret.getUniqueIndexes().add(meta);
            }else{
                ret.getIndexes().add(meta);
            }
        }
    }



}
