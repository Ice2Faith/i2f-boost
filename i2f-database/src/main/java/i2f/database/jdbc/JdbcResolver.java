package i2f.database.jdbc;

import i2f.database.jdbc.data.BindSql;
import i2f.database.jdbc.data.QueryColumn;
import i2f.database.jdbc.data.QueryResult;

import java.sql.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:58
 * @desc
 */
public class JdbcResolver {
    public static Connection getConnection(String driver,
                                           String url) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url);
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           String username,
                                           String password) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url, username, password);
    }

    public static Connection getConnection(String driver,
                                           String url,
                                           Properties properties) throws SQLException {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
        return DriverManager.getConnection(url, properties);
    }

    public static Connection begin(Connection conn) throws SQLException {
        conn.setAutoCommit(false);
        return conn;
    }

    public static Connection auto(Connection conn) throws SQLException {
        conn.setAutoCommit(true);
        return conn;
    }

    public static Connection commit(Connection conn) throws SQLException {
        conn.commit();
        return conn;
    }

    public static Connection rollback(Connection conn) throws SQLException {
        conn.rollback();
        return conn;
    }

    @FunctionalInterface
    public interface SQLFunction<T, R> {
        R apply(T t) throws SQLException;
    }

    @FunctionalInterface
    public interface SQLBiFunction<T, U, R> {
        R apply(T t, U u) throws SQLException;
    }

    public static <E, R> R transaction(Connection conn, SQLBiFunction<Connection, E, R> operation, E arg) throws SQLException {
        boolean bak = conn.getAutoCommit();
        begin(conn);
        try {
            R ret = operation.apply(conn, arg);
            commit(conn);
            return ret;
        } catch (Exception e) {
            rollback(conn);
            if (e instanceof SQLException) {
                throw (SQLException) e;
            } else {
                throw new SQLException(e.getMessage(), e);
            }
        } finally {
            conn.setAutoCommit(bak);
        }
    }

    public static List<Object> transaction(Connection conn, List<BindSql> sqls) throws SQLException {
        return transaction(conn, (connection, sqlList) -> {
            List<Object> ret = new ArrayList<>();
            for (BindSql sql : sqlList) {
                if (sql.isUpdate()) {
                    int val = update(connection, sql.getSql(), sql.getArgs());
                    ret.add(val);
                } else {
                    QueryResult val = query(connection, sql.getSql(), sql.getArgs());
                    ret.add(val);
                }
            }
            return ret;
        }, sqls);
    }

    public static QueryResult query(Connection conn, BindSql sql) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs());
    }

    public static <R> R query(Connection conn, BindSql sql, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        return query(conn, sql.getSql(), sql.getArgs(), resultSetHandler);
    }

    public static QueryResult query(Connection conn, String sql, List<?> args) throws SQLException {
        return query(conn, sql, args, JdbcResolver::parseResultSet);
    }

    public static <R> R query(Connection conn, String sql, List<?> args, SQLFunction<ResultSet, R> resultSetHandler) throws SQLException {
        PreparedStatement stat = preparedStatement(conn, sql, args);
        ResultSet rs = stat.executeQuery();
        R ret = resultSetHandler.apply(rs);
        if (!rs.isClosed()) {
            rs.close();
        }
        stat.close();
        return ret;
    }

    public static int update(Connection conn, BindSql sql) throws SQLException {
        return update(conn, sql.getSql(), sql.getArgs());
    }

    public static int update(Connection conn, String sql, List<?> args) throws SQLException {
        PreparedStatement stat = preparedStatement(conn, sql, args);
        int ret = stat.executeUpdate();
        stat.close();
        return ret;
    }

    public static boolean call(Connection conn, String sql) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, null, null);
        return (Boolean) map.get(-1);
    }

    public static boolean call(Connection conn, String sql, List<?> args) throws SQLException {
        Map<Integer, Object> map = call(conn, sql, args, null);
        return (Boolean) map.get(-1);
    }

    /**
     * 返回值为outParams中指定的出参
     * 特别的，key=-1表示执行的结果boolean值
     *
     * @param conn
     * @param sql
     * @param args
     * @param outParams
     * @return
     * @throws SQLException
     */
    public static Map<Integer, Object> call(Connection conn, String sql, List<?> args, Map<Integer, SQLType> outParams) throws SQLException {
        CallableStatement stat = callStatement(conn, sql, args, outParams);
        boolean ok = stat.execute();
        Map<Integer, Object> ret = new LinkedHashMap<>();
        ret.put(-1, ok);
        if (outParams != null) {
            for (Map.Entry<Integer, SQLType> entry : outParams.entrySet()) {
                Object val = stat.getObject(entry.getKey() + 1);
                ret.put(entry.getKey(), val);
            }
        }
        return ret;
    }

    /**
     * sql= {call producer_name(?,?,?)}
     *
     * @param sql
     * @return
     */
    public static String callSql(String sql) {
        sql = sql.trim();
        if (!sql.startsWith("{")) {
            if (!sql.toLowerCase().startsWith("call")) {
                sql = "call " + sql;
            }
            sql = "{" + sql;
        }
        if (!sql.endsWith("}")) {
            sql = sql + "}";
        }
        return sql;
    }

    public static CallableStatement callStatement(Connection conn, String sql) throws SQLException {
        return callStatement(conn, sql, null, null);
    }

    public static CallableStatement callStatement(Connection conn, String sql, List<?> args) throws SQLException {
        return callStatement(conn, sql, args, null);
    }

    /**
     * args 每个参数位置都是需要的，包括出参位置，出参可以设为任意值补位，因为会被忽略
     * outParams 指定哪些是出参
     * 举例：
     * 调用存过：sp_test(in,in,out,in,out)
     * 则对应的入参为：
     * sql= {call sp_test(?,?,?,?,?)}
     * args= [1,1,null,1,null]
     * outParams= {
     * 2: JDBCType.NUMBER,
     * 4: JDBCType.VARCHAR
     * }
     *
     * @param conn
     * @param sql
     * @param args
     * @param outParams
     * @return
     * @throws SQLException
     */
    public static CallableStatement callStatement(Connection conn, String sql, List<?> args, Map<Integer, SQLType> outParams) throws SQLException {
        Set<Integer> outs = new HashSet<>();
        if (outParams != null) {
            outs.addAll(outParams.keySet());
        }
        CallableStatement stat = conn.prepareCall(sql);
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                if (outs.contains(i - 1)) {
                    stat.registerOutParameter(i, outParams.get(i - 1));
                } else {
                    stat.setObject(i, arg);
                }
                i++;
            }
        }
        return stat;
    }

    public static PreparedStatement preparedStatement(Connection conn, BindSql sql) throws SQLException {
        return preparedStatement(conn, sql.getSql(), sql.getArgs());
    }

    public static PreparedStatement preparedStatement(Connection conn, String sql, List<?> args) throws SQLException {
        PreparedStatement stat = conn.prepareStatement(sql);
        if (args != null) {
            int i = 1;
            for (Object arg : args) {
                stat.setObject(i, arg);
                i++;
            }
        }
        return stat;
    }

    public static QueryResult parseResultSet(ResultSet rs) throws SQLException {
        QueryResult ret = new QueryResult();
        List<QueryColumn> columns = new ArrayList<>();
        List<Map<String, Object>> rows = new LinkedList<>();
        ret.setColumns(columns);
        ret.setRows(rows);

        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            QueryColumn col = new QueryColumn();
            col.setIndex(i);
            col.setName(metaData.getColumnName(i + 1));
            col.setCatalog(metaData.getCatalogName(i + 1));
            col.setClazzName(metaData.getColumnClassName(i + 1));
            col.setDisplaySize(metaData.getColumnDisplaySize(i + 1));
            col.setLabel(metaData.getColumnLabel(i + 1));
            col.setType(metaData.getColumnType(i + 1));
            col.setTypeName(metaData.getColumnTypeName(i + 1));
            col.setPrecision(metaData.getPrecision(i + 1));
            col.setScale(metaData.getScale(i + 1));
            col.setSchema(metaData.getSchemaName(i + 1));
            col.setTable(metaData.getTableName(i + 1));
            col.setNullable(metaData.isNullable(i + 1) != ResultSetMetaData.columnNoNulls);
            col.setAutoIncrement(metaData.isAutoIncrement(i + 1));
            col.setReadonly(metaData.isReadOnly(i + 1));
            col.setWritable(metaData.isWritable(i + 1));
            col.setCaseSensitive(metaData.isCaseSensitive(i + 1));
            col.setCurrency(metaData.isCurrency(i + 1));
            col.setDefinitelyWritable(metaData.isDefinitelyWritable(i + 1));
            col.setSearchable(metaData.isSearchable(i + 1));
            col.setSigned(metaData.isSigned(i + 1));

            columns.add(col);
        }
        while (rs.next()) {
            Map<String, Object> map = new LinkedHashMap<>();
            for (int i = 0; i < columnCount; i++) {
                Object val = rs.getObject(i + 1);
                map.put(columns.get(i).getName(), val);
            }
            rows.add(map);
        }

        rs.close();

        return ret;
    }
}
