package i2f.database.metadata;

import i2f.database.jdbc.data.QueryResult;
import i2f.database.metadata.data.TableMeta;
import i2f.database.metadata.impl.gbase.GbaseDatabaseMetadataProvider;
import i2f.database.metadata.impl.mysql.MysqlDatabaseMetadataProvider;
import i2f.database.metadata.impl.oracle.OracleDatabaseMetadataProvider;
import i2f.database.metadata.impl.postgresql.PostgreSqlDatabaseMetadataProvider;
import i2f.database.metadata.impl.sqlite3.Sqlite3DatabaseMetadataProvider;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * @author Ice2Faith
 * @date 2024/3/14 10:48
 * @desc
 */
public interface DatabaseMetadataProvider {
    static List<Driver> getSpiDrivers(){
        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        List<Driver> ret=new ArrayList<>();
        for (Driver driver : loader) {
            ret.add(driver);
        }
        return ret;
    }
    static String getProductName(Connection conn) throws SQLException {
        return conn.getMetaData().getDatabaseProductName();
    }
    static String getProductVersion(Connection conn) throws SQLException {
        return conn.getMetaData().getDatabaseProductVersion();
    }
    static String getDriverName(Connection conn) throws SQLException {
        return conn.getMetaData().getDriverName();
    }
    static String getDriverVersion(Connection conn) throws SQLException {
        return conn.getMetaData().getDriverVersion();
    }
    static String getConnectionUrl(Connection conn) throws SQLException {
        return conn.getMetaData().getURL();
    }
    static String getUsername(Connection conn) throws SQLException {
        return conn.getMetaData().getUserName();
    }
    static DatabaseMetadataProvider findProvider(Connection conn) throws SQLException {
        String productName = conn.getMetaData().getDatabaseProductName().toLowerCase();
        if(productName.contains("mysql")){
            return new MysqlDatabaseMetadataProvider();
        }else if(productName.contains("oracle")){
            return new OracleDatabaseMetadataProvider();
        }else if(productName.contains("gbase")){
            return new GbaseDatabaseMetadataProvider();
        }else if(productName.contains("sqlite")){
            return new Sqlite3DatabaseMetadataProvider();
        }else if(productName.contains("postgresql")){
            return new PostgreSqlDatabaseMetadataProvider();
        }
        throw new IllegalStateException("un-support metadata provider for product: "+productName);
    }

    List<String> getDataTypes(Connection conn) throws SQLException;

    List<String> getCatalogs(Connection conn) throws SQLException;

    List<String> getSchemas(Connection conn) throws SQLException;

    List<String> getDatabases(Connection conn) throws SQLException;

    List<TableMeta> getTables(Connection conn,String database) throws SQLException;

    TableMeta getTableInfo(Connection conn,String database,String table) throws SQLException;

    TableMeta getTableInfoByQuery(QueryResult result) throws SQLException;

    TableMeta getTableInfoByQuery(ResultSet rs) throws SQLException;

    TableMeta getTableInfoByQuery(Connection conn,String table) throws SQLException;
}
