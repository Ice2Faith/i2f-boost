package i2f.core.database.jdbc.core;

import i2f.core.annotations.remark.Author;
import i2f.core.database.jdbc.data.DBResultList;
import i2f.core.database.jdbc.data.PageContextData;
import i2f.core.database.jdbc.data.PageMeta;
import i2f.core.type.date.Dates;
import i2f.core.type.str.Appender;
import lombok.Data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2021/9/27
 */
@Author("i2f")
@Data
public class JdbcProvider {
    public static  boolean showLog=true;

    private TransactionManager transactionManager;

    public static void logout(Date date,Statement stat,String sql,List<Object> params){
        if(showLog){
            String log= Appender.builder()
                    .adds("JdbcLogOut -> time:", Dates.format(date)).line()
                    .adds("\tstat: ",stat).line()
                    .adds("\tsql : ",sql).line()
                    .add("\tparams : ")
                    .addCollection(params,",","[","]")
                    .get();
            System.out.println(log);
        }
    }
    public static void logout(Date date,Object ... objs){
        if(showLog){
            String log= Appender.builder()
                    .adds("JdbcLogOut -> time:", Dates.format(date), " : ")
                    .adds(objs)
                    .get();
            System.out.println(log);
        }
    }


    public JdbcProvider(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public JdbcProvider(IJdbcMeta meta) throws SQLException {
        this.transactionManager = new TransactionManager(meta);
    }

    public static void registryDriverClass(String className) throws SQLException {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Db driver class not found, className=" + className, e);
        }
    }

    public Connection getConnection() throws SQLException {
        return transactionManager.getConnection();
    }

    public static Connection getConnection(IJdbcMeta meta) throws SQLException {
        registryDriverClass(meta.getDriver());
        Connection conn= DriverManager.getConnection(meta.getUrl(), meta.getUsername(), meta.getPassword());
        return conn;
    }

    public static void closeRes(Connection conn, Statement stat, ResultSet rs) throws SQLException {
        if(rs!=null){
            rs.close();
        }
        if(stat!=null){
            stat.close();
        }
        if(conn!=null){
            conn.close();
        }
    }

    public boolean execute(String prepareSql, Object ... params) throws SQLException {
        boolean ret= execute(getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public boolean executeRaw(String prepareSql, List<Object> params) throws SQLException {
        boolean ret= executeRaw(getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public static boolean execute(Connection conn, String prepareSql, Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return executeRaw(conn,prepareSql,args);
    }
    public static boolean executeRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat=PreparedStatementBuilder.makeByList(conn,prepareSql,params);
        Date date=new Date(System.currentTimeMillis());
        logout(date,stat,prepareSql,params);
        boolean rs= stat.execute();
        logout(date,"execute result : ",rs);
        closeRes(null,stat,null);
        return rs;
    }

    public int update(String prepareSql, Object ... params) throws SQLException {
        int ret= update(getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public int updateRaw(String prepareSql, List<Object> params) throws SQLException {
        int ret= updateRaw(getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public static int update(Connection conn, String prepareSql, Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return updateRaw(conn,prepareSql,args);
    }

    public static int updateRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat = PreparedStatementBuilder.makeByList(conn, prepareSql, params);
        Date date = new Date(System.currentTimeMillis());
        logout(date, stat, prepareSql, params);
        int rs = stat.executeUpdate();
        logout(date, "update result : ", rs);
        closeRes(null, stat, null);
        return rs;
    }

    public DBResultList query(String prepareSql, Object... params) throws SQLException {
        DBResultList ret = query(getConnection(), prepareSql, params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }

    public DBResultList queryRaw(String prepareSql, List<Object> params) throws SQLException {
        DBResultList ret = queryRaw(getConnection(), prepareSql, params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }

    public static DBResultList query(Connection conn, String prepareSql, Object... params) throws SQLException {
        List<Object> args = new ArrayList<>(params.length);
        for (Object item : params) {
            args.add(item);
        }
        return queryRaw(conn, prepareSql, args);
    }

    public static DBResultList queryRaw(Connection conn, String prepareSql, List<Object> params) throws SQLException {
        PreparedStatement stat = PreparedStatementBuilder.makeByList(conn, prepareSql, params);
        Date date = new Date(System.currentTimeMillis());
        logout(date, stat, prepareSql, params);
        ResultSet rs = stat.executeQuery();
        DBResultList ret = DBResultList.of(rs);
        logout(date, "query result : ", ret.getCount());
        closeRes(null, stat, rs);
        return ret;
    }


    public PageContextData page(PageMeta page, String prepareSql, Object ... params) throws SQLException {
        PageContextData ret= page(page,getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public static PageContextData page(PageMeta page,Connection conn,String prepareSql,Object ... params) throws SQLException {
        List<Object> args=new ArrayList<>(params.length);
        for(Object item : params){
            args.add(item);
        }
        return pageRaw(page,conn,prepareSql,args);
    }
    public PageContextData pageRaw(PageMeta page,String prepareSql,List<Object> params) throws SQLException {
        PageContextData ret= pageRaw(page,getConnection(),prepareSql,params);
        if (!transactionManager.isKeepConnect()) {
            transactionManager.close();
        }
        return ret;
    }
    public static PageContextData pageRaw(PageMeta page,Connection conn,String prepareSql,List<Object> params) throws SQLException {
        return PageProvider.queryPage(page,conn,prepareSql,params);
    }

}
