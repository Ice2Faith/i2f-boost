package i2f.core.db.core;


import i2f.core.annotations.notice.IfNull;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.db.impl.DefaultTableMetaFilter;
import i2f.core.functional.common.IFilter;
import i2f.core.jdbc.core.JdbcProvider;
import i2f.core.jdbc.core.TransactionManager;
import i2f.core.jdbc.data.DBResultList;
import i2f.core.jdbc.type.DbType;

import java.sql.*;
import java.util.*;

/**
 * @author ltb
 * @date 2021/8/25
 */
@Author("i2f")
public class DbResolver {

    public static List<TableMeta> getAllTables(Connection conn, boolean withColumns) throws SQLException {
        return getAllTables(conn, withColumns, null);
    }

    public static List<TableMeta> getAllTables(Connection conn, boolean withColumns, @Nullable @IfNull("DefaultTableMetaFilter") IFilter<TableMeta> filter) throws SQLException {
        List<TableMeta> ret = new ArrayList<>();
        String srcCatalog = conn.getCatalog();
        String srcSchema = conn.getSchema();
        if (filter == null) {
            filter = new DefaultTableMetaFilter();
        }
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet schemas = dbMeta.getSchemas();
        while (schemas.next()) {
            String schema = schemas.getString("TABLE_SCHEM");
            String catalog = schemas.getString("TABLE_CATALOG");
        }
        schemas.close();
        ResultSet tables = dbMeta.getTables(null, null, "%", null);
        while (tables.next()) {
            TableMeta meta = new TableMeta();
            inflateTableAttributes(tables, meta);

            if (filter.test(meta)) {
                if (withColumns) {
                    try {
                        if (meta.getCatalog() != null) {
                            conn.setCatalog(meta.getCatalog());
                        }
                        if (meta.getSchema() != null) {
                            conn.setSchema(meta.getSchema());
                        }
                        TableMeta cmeta = getTableMeta(conn, meta.getTable());
                        meta.setColumns(cmeta.getColumns());
                        if (meta.getSchema() == null) {
                            meta.setSchema(cmeta.getSchema());
                        }
                    } catch (Exception e) {

                    }
                }
                ret.add(meta);
            }

        }
        tables.close();
        conn.setCatalog(srcCatalog);
        conn.setSchema(srcSchema);
        return ret;

    }

    private static void inflateTableAttributes(ResultSet tables, TableMeta meta) throws SQLException {
        String type = tables.getString("TABLE_TYPE");
        String catalog = tables.getString("TABLE_CAT");
        String schema = tables.getString("TABLE_SCHEM");
        String table = tables.getString("TABLE_NAME");
        String remark = tables.getString("REMARKS");
        String typeCatalog = tables.getString("TYPE_CAT");
        String typeSchema = tables.getString("TYPE_SCHEM");
        String typeName = tables.getString("TYPE_NAME");
        String selfRefColName = tables.getString("SELF_REFERENCING_COL_NAME");
        String refGeneration = tables.getString("REF_GENERATION");

        meta.setCatalog(catalog);
        meta.setSchema(schema);
        meta.setTable(table);
        meta.setType(type);
        meta.setRemark(remark);
        meta.setTypeCatalog(typeCatalog);
        meta.setTypeSchema(typeSchema);
        meta.setTypeName(typeName);
        meta.setSelfReferencingColName(selfRefColName);
        meta.setRefGeneration(refGeneration);
    }

    public static TableMeta getTableMeta(Connection conn, String tableName) throws SQLException {
        String sql = "select * from " + tableName + " where 1!=1";
        Statement stat = conn.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        ResultSetMetaData meta = rs.getMetaData();
        TableMeta ret = inflateTableMeta(conn, meta);
        if (ret.getTable() == null || "".equals(ret.getTable())) {
            ret.setTable(tableName);
        }
        rs.close();
        stat.close();

        JdbcProvider provider = new JdbcProvider(new TransactionManager(conn));
        // 由于目前provider的设计上，没有开启事务，则执行一次直接关闭连接，因此使用一个事务
        // 后续provider可能改变策略
        provider.getTransactionManager().keepConnect(true);
        String connUrl = conn.getMetaData().getURL();
        DbType dbType = DbType.typeOfJdbcUrl(connUrl);
        if (ret.getRemark() == null) {
            // 处理oracle连接获取不到表comment问题
            if (dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C) {
                DBResultList rd = provider.query("SELECT COMMENTS FROM user_tab_comments WHERE TABLE_NAME = ?", ret.getTable());
                if (!rd.isEmpty()) {
                    ret.setRemark(rd.getStringValue());
                }
            }else if(dbType==DbType.MYSQL) {
                DBResultList rd = provider.query("select TABLE_COMMENT from information_schema.`TABLES` where TABLE_SCHEMA = ? and TABLE_NAME = ?", ret.getCatalog(), ret.getTable());
                if (!rd.isEmpty()) {
                    ret.setRemark(rd.getStringValue());
                }
            }
        }

        boolean hasColumnRemark = false;
        for (TableColumnMeta item : ret.getColumns()) {
            if (item.getRemark() != null) {
                hasColumnRemark = true;
                break;
            }
        }

        if (!hasColumnRemark) {
            if (ret.getTable() != null && !"".equals(ret.getTable())) {
                // 处理oracle连接获取不到列comment问题
                if (dbType == DbType.ORACLE || dbType == DbType.ORACLE_12C) {
                    DBResultList rd = provider.query("SELECT COLUMN_NAME,COMMENTS FROM user_col_comments WHERE TABLE_NAME = ?", ret.getTable());
                    Map<String, String> columnCommentMap = new HashMap<>();
                    if (!rd.isEmpty()) {
                        for (int i = 0; i < rd.size(); i++) {
                            columnCommentMap.put((rd.getStringValue(i, 0)).toLowerCase(), rd.getStringValue(i, 1));
                        }
                    }
                    for (TableColumnMeta item : ret.getColumns()) {
                        if (item.getRemark() == null) {
                            item.setRemark(columnCommentMap.get(item.getName().toLowerCase()));
                        }
                    }
                }else if(dbType==DbType.MYSQL) {
                    DBResultList rd = provider.query("select COLUMN_NAME,COLUMN_COMMENT from information_schema.`COLUMNS where TABLE_SCHEMA=? and TABLE_NAME =?", ret.getCatalog(), ret.getTable());
                    Map<String, String> columnCommentMap = new HashMap<>();
                    if (!rd.isEmpty()) {
                        for (int i = 0; i < rd.size(); i++) {
                            columnCommentMap.put((rd.getStringValue(i, 0)).toLowerCase(), rd.getStringValue(i, 1));
                        }
                    }
                    for (TableColumnMeta item : ret.getColumns()) {
                        if (item.getRemark() == null) {
                            item.setRemark(columnCommentMap.get(item.getName().toLowerCase()));
                        }
                    }
                }
            }
        }
        provider.getTransactionManager().keepConnect(false);

        return ret;
    }

    public static TableMeta inflateTableMeta(Connection conn, ResultSetMetaData meta) throws SQLException {
        TableMeta ret = new TableMeta();
        String schema = meta.getSchemaName(1);
        String table = meta.getTableName(1);
        String catalog = meta.getCatalogName(1);
        ret.setSchema(schema);
        ret.setTable(table);
        ret.setCatalog(catalog);
        DatabaseMetaData dbMeta = conn.getMetaData();
        ResultSet tables = dbMeta.getTables(catalog, schema, table, null);
        if (tables.next()) {
            inflateTableAttributes(tables, ret);
        }
        tables.close();
        List<TableColumnMeta> cols = inflateTableColumnMeta(dbMeta, meta);
        ret.setColumns(cols);

        return ret;
    }

    public static List<TableColumnMeta> inflateTableColumnMeta(DatabaseMetaData dbMeta, ResultSetMetaData meta) throws SQLException {
        List<TableColumnMeta> ret = new ArrayList<>();
        Map<String, String> primaryKeys = new HashMap<>();
        Map<String, String> uniques = new HashMap<>();
        Map<String, String> indexes = new HashMap<>();
        Map<String, String> indexTypes = new HashMap<>();
        if (dbMeta != null) {
            ResultSet rs = dbMeta.getPrimaryKeys(meta.getCatalogName(1), meta.getSchemaName(1), meta.getTableName(1));
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String pkName = rs.getString("PK_NAME");
                String keySeq = rs.getString("KEY_SEQ");
                primaryKeys.put(colName, pkName);
            }
            rs.close();
            rs = dbMeta.getIndexInfo(meta.getCatalogName(1), meta.getSchemaName(1), meta.getTableName(1), true, true);
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String indexName = rs.getString("INDEX_NAME");
                short type = rs.getShort("TYPE");
                uniques.put(colName, indexName);
            }
            rs.close();
            rs = dbMeta.getIndexInfo(meta.getCatalogName(1), meta.getSchemaName(1), meta.getTableName(1), false, true);
            while (rs.next()) {
                String colName = rs.getString("COLUMN_NAME");
                String indexName = rs.getString("INDEX_NAME");
                short type = rs.getShort("TYPE");
                String stype = "";
                switch (type) {
                    case 0:
                        stype = "Statistic";
                        break;
                    case 1:
                        stype = "Clustered";
                        break;
                    case 2:
                        stype = "Hashed";
                        break;
                    case 3:
                        stype = "Other";
                        break;
                    default:
                        break;
                }
                indexes.put(colName, indexName);
                indexTypes.put(colName, stype);
            }
            rs.close();
        }
        int colCount = meta.getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            TableColumnMeta col = new TableColumnMeta();
            String colName = meta.getColumnName(i);
            int colType = meta.getColumnType(i);
            String colTypeName = meta.getColumnTypeName(i);
            String colLabel = meta.getColumnLabel(i);
            String colClassName = meta.getColumnClassName(i);
            int colDisplaySize = meta.getColumnDisplaySize(i);
            String catalogName = meta.getCatalogName(i);
            String tableName = meta.getTableName(i);
            String schemaName = meta.getSchemaName(i);
            int precision = meta.getPrecision(i);
            int scale = meta.getScale(i);

            col.setName(colName);
            col.setType(colType);
            col.setTypeName(colTypeName);
            col.setDisplaySize(colDisplaySize);
            col.setJavaTypeString(colClassName);
            col.setCatalogName(catalogName);
            col.setLabel(colLabel);
            col.setTableName(tableName);
            col.setSchemaName(schemaName);
            col.setPrecision(precision);
            col.setScale(scale);
            if (primaryKeys.containsKey(colName)) {
                col.setPrimaryKeyName(primaryKeys.get(colName));
            }
            col.setIsPrimaryKey(primaryKeys.containsKey(colName) ? "YES" : "NO");

            if (uniques.containsKey(colName)) {
                col.setUniqueKeyName(uniques.get(colName));
            }
            col.setIsUnique(uniques.containsKey(colName) ? "YES" : "NO");

            if (indexes.containsKey(colName)) {
                col.setIndexKeyName(indexes.get(colName));
                col.setIndexType(indexTypes.get(colName));
            }
            col.setIsIndex(indexes.containsKey(colName) ? "YES" : "NO");

            if (dbMeta != null) {
                ResultSet rs = dbMeta.getColumns(col.getCatalogName(), col.getSchemaName(), col.getTableName(), col.getName());
                if (rs.next()) {
                    String remark = rs.getString("REMARKS");
                    String dataType = rs.getString("DATA_TYPE");
                    String columnSize = rs.getString("COLUMN_SIZE");
                    String bufferLength = rs.getString("BUFFER_LENGTH");
                    String decimalDigits = rs.getString("DECIMAL_DIGITS");
                    String numPrecRadix = rs.getString("NUM_PREC_RADIX");
                    String nullable = rs.getString("NULLABLE");
                    String columnDef = rs.getString("COLUMN_DEF");
                    String sqlDataType = rs.getString("SQL_DATA_TYPE");
                    String charOctetLength = rs.getString("CHAR_OCTET_LENGTH");
                    String ordinalPosition = rs.getString("ORDINAL_POSITION");
                    String isNullable = rs.getString("IS_NULLABLE");
                    String sourceDataType = rs.getString("SOURCE_DATA_TYPE");
                    String isAutoincrement = rs.getString("IS_AUTOINCREMENT");
                    String isGeneratedColumn = rs.getString("IS_GENERATEDCOLUMN");

                    col.setRemark(remark);
                    col.setDataType(dataType);
                    col.setColumnSize(columnSize);
                    col.setBufferLength(bufferLength);
                    col.setDecimalDigits(decimalDigits);
                    col.setNumPrecRadix(numPrecRadix);
                    col.setNullable(nullable);
                    col.setColumnDef(columnDef);
                    col.setSqlDataType(sqlDataType);
                    col.setCharOctetLength(charOctetLength);
                    col.setOrdinalPosition(ordinalPosition);
                    col.setIsNullable(isNullable);
                    col.setSourceDataType(sourceDataType);
                    col.setIsAutoincrement(isAutoincrement);
                    col.setIsGeneratedColumn(isGeneratedColumn);
                }
                rs.close();
            }

            try {
                Class clazz = Class.forName(colClassName);
                col.setJavaType(clazz);
            } catch (Throwable t) {

            }
            ret.add(col);
        }
        ret.sort(new Comparator<TableColumnMeta>() {
            @Override
            public int compare(TableColumnMeta o1, TableColumnMeta o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        return ret;
    }
}
