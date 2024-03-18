package i2f.database.metadata.test;

import i2f.database.jdbc.JdbcResolver;
import i2f.database.jdbc.data.QueryResult;
import i2f.database.metadata.DatabaseMetadataProvider;

import java.sql.*;
import java.util.*;

/**
 * @author Ice2Faith
 * @date 2024/3/13 17:04
 * @desc
 */
public class TestDatabaseMetadata {

    public static String stringify(Object obj) {
        return String.valueOf(obj);
    }

    public static void main(String[] args) throws Exception {

        ServiceLoader<Driver> loader = ServiceLoader.load(Driver.class);
        for (Driver driver : loader) {
            System.out.println(driver);
        }

        parseOracle();

        parseGbase();

        parseMysql();

        parseSqlite();

        parsePostgreSql();

        System.out.println("ok");
    }

    public static void parsePostgreSql() throws Exception {
        Class.forName("org.postgresql.Driver");

        Connection conn=DriverManager.getConnection("jdbc:postgresql://localhost:5432/test_db",
                "postgres","123456");

        DatabaseMetadataProvider provider=DatabaseMetadataProvider.findProvider(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("---------------------user name ----------------------------------");
        System.out.println(metaData.getUserName());

        System.out.println("-------------------product name------------------------------------");
        System.out.println(metaData.getDatabaseProductName());

        System.out.println("---------------------product version----------------------------------");
        System.out.println(metaData.getDatabaseProductVersion());

        System.out.println("----------------------driver name---------------------------------");
        System.out.println(metaData.getDriverName());

        System.out.println("-----------------------driver version--------------------------------");
        System.out.println(metaData.getDriverVersion());

        System.out.println("-------------------------schemas------------------------------");
        System.out.println(stringify(provider.getSchemas(conn)));

        System.out.println("--------------------------catalogs-----------------------------");
        System.out.println(stringify(provider.getCatalogs(conn)));

        System.out.println("--------------------------databases-----------------------------");
        System.out.println(stringify(provider.getDatabases(conn)));

        System.out.println("--------------------------tables-----------------------------");
        System.out.println(stringify(provider.getTables(conn,"test_db")));

        System.out.println("---------------------table info----------------------------------");
        System.out.println(stringify(provider.getTableInfo(conn,"test_db","sys_user")));

        String sql="SELECT col.table_name, col.column_name, col.ordinal_position, d.description, \n" +
                "col.table_catalog,col.table_schema, \n" +
                "col.column_default,col.is_nullable,col.data_type,col.character_maximum_length, \n" +
                "col.numeric_precision,col.numeric_scale \n" +
                "FROM information_schema.columns col \n" +
                "JOIN pg_class c ON c.relname = col.table_name \n" +
                "LEFT JOIN pg_description d ON d.objoid = c.oid AND d.objsubid = col.ordinal_position \n" +
                "WHERE col.table_catalog = ? \n" +
                "and col.table_name = ? \n" +
                "and col.table_schema = 'public' \n" +
                "ORDER BY col.table_name, col.ordinal_position ";

        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, "test_db");
        stat.setString(2, "sys_user");

        System.out.println("-------------------------------------------------------");
        System.out.println(stringify(JdbcResolver.parseResultSet(stat.executeQuery())));
        stat.close();


        conn.close();
    }

    public static void parseSqlite() throws Exception {
        Class.forName("org.sqlite.JDBC");

        Connection conn = DriverManager.getConnection("jdbc:sqlite:E:\\MySystemDefaultFiles\\Desktop\\12代码测试\\ce_dict\\ce_dict_db.db");

        DatabaseMetadataProvider provider=DatabaseMetadataProvider.findProvider(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("---------------------user name ----------------------------------");
        System.out.println(metaData.getUserName());

        System.out.println("-------------------product name------------------------------------");
        System.out.println(metaData.getDatabaseProductName());

        System.out.println("---------------------product version----------------------------------");
        System.out.println(metaData.getDatabaseProductVersion());

        System.out.println("----------------------driver name---------------------------------");
        System.out.println(metaData.getDriverName());

        System.out.println("-----------------------driver version--------------------------------");
        System.out.println(metaData.getDriverVersion());

        System.out.println("-------------------------schemas------------------------------");
        System.out.println(stringify(provider.getSchemas(conn)));

        System.out.println("--------------------------catalogs-----------------------------");
        System.out.println(stringify(provider.getCatalogs(conn)));

        System.out.println("--------------------------databases-----------------------------");
        System.out.println(stringify(provider.getDatabases(conn)));

        System.out.println("--------------------------tables-----------------------------");
        System.out.println(stringify(provider.getTables(conn,null)));

        System.out.println("---------------------table info----------------------------------");
        System.out.println(stringify(provider.getTableInfo(conn,null,"ce_dict")));

        if(true){
            String sql="PRAGMA table_info("+"sys_user"+")";

            PreparedStatement stat = conn.prepareStatement(sql);

            System.out.println("-------------------------------------------------------");
            System.out.println(stringify(JdbcResolver.parseResultSet(stat.executeQuery())));
            stat.close();
        }

        if(true){
            String sql="select * from `sqlite_master` where type=? and name=?";

            PreparedStatement stat = conn.prepareStatement(sql);
            stat.setString(1,"table");
            stat.setString(2,"ce_dict");

            System.out.println("-------------------------------------------------------");
            QueryResult rs = JdbcResolver.parseResultSet(stat.executeQuery());
            stat.close();

            System.out.println(stringify(rs));
        }


        conn.close();
    }

    public static void parseMysql() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/i2f_proj?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf8&useSSL=false",
                "root", "123456");


        DatabaseMetadataProvider provider=DatabaseMetadataProvider.findProvider(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("---------------------user name ----------------------------------");
        System.out.println(metaData.getUserName());

        System.out.println("-------------------product name------------------------------------");
        System.out.println(metaData.getDatabaseProductName());

        System.out.println("---------------------product version----------------------------------");
        System.out.println(metaData.getDatabaseProductVersion());

        System.out.println("----------------------driver name---------------------------------");
        System.out.println(metaData.getDriverName());

        System.out.println("-----------------------driver version--------------------------------");
        System.out.println(metaData.getDriverVersion());

        System.out.println("-------------------------schemas------------------------------");
        System.out.println(stringify(provider.getSchemas(conn)));

        System.out.println("--------------------------catalogs-----------------------------");
        System.out.println(stringify(provider.getCatalogs(conn)));

        System.out.println("--------------------------databases-----------------------------");
        System.out.println(stringify(provider.getDatabases(conn)));

        System.out.println("--------------------------tables-----------------------------");
        System.out.println(stringify(provider.getTables(conn,"i2f_proj")));

        System.out.println("---------------------table info----------------------------------");
        System.out.println(stringify(provider.getTableInfo(conn,"i2f_proj","sys_user")));

        String sql = "select *\n" +
                "from information_schema.COLUMNS\n" +
                "where TABLE_SCHEMA=?\n" +
                "and TABLE_NAME=?\n" +
                "order by ORDINAL_POSITION asc ";

        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, "i2f_proj");
        stat.setString(2, "sys_user");

        System.out.println("-------------------------------------------------------");
        System.out.println(stringify(JdbcResolver.parseResultSet(stat.executeQuery())));
        stat.close();


        conn.close();
    }

    public static void parseGbase() throws Exception {
        Class.forName("com.gbase.jdbc.Driver");

        Connection conn = DriverManager.getConnection("jdbc:gbase://localhost:5258/test_db",
                "test_db", "123456");


        DatabaseMetadataProvider provider=DatabaseMetadataProvider.findProvider(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("---------------------user name ----------------------------------");
        System.out.println(metaData.getUserName());

        System.out.println("-------------------product name------------------------------------");
        System.out.println(metaData.getDatabaseProductName());

        System.out.println("---------------------product version----------------------------------");
        System.out.println(metaData.getDatabaseProductVersion());

        System.out.println("----------------------driver name---------------------------------");
        System.out.println(metaData.getDriverName());

        System.out.println("-----------------------driver version--------------------------------");
        System.out.println(metaData.getDriverVersion());

        System.out.println("-------------------------schemas------------------------------");
        System.out.println(stringify(provider.getSchemas(conn)));

        System.out.println("--------------------------catalogs-----------------------------");
        System.out.println(stringify(provider.getCatalogs(conn)));

        System.out.println("--------------------------databases-----------------------------");
        System.out.println(stringify(provider.getDatabases(conn)));

        System.out.println("--------------------------tables-----------------------------");
        System.out.println(stringify(provider.getTables(conn,"spsv_dev")));

        System.out.println("---------------------table info----------------------------------");
        System.out.println(stringify(provider.getTableInfo(conn,"spsv_dev","sys_config_item")));

        String sql = "select *\n" +
                "from information_schema.COLUMNS\n" +
                "where TABLE_SCHEMA=?\n" +
                "and TABLE_NAME=?\n" +
                "order by ORDINAL_POSITION asc ";

        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, "spsv_dev");
        stat.setString(2, "dwd_uuser");

        System.out.println("-------------------------------------------------------");
        System.out.println(stringify(JdbcResolver.parseResultSet(stat.executeQuery())));
        stat.close();


        conn.close();
    }

    public static void parseOracle() throws Exception {

        Class.forName("oracle.jdbc.OracleDriver");

        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521/orcl",
                "test_db", "123456");

        DatabaseMetadataProvider provider=DatabaseMetadataProvider.findProvider(conn);

        DatabaseMetaData metaData = conn.getMetaData();
        System.out.println("---------------------user name ----------------------------------");
        System.out.println(metaData.getUserName());

        System.out.println("-------------------product name------------------------------------");
        System.out.println(metaData.getDatabaseProductName());

        System.out.println("---------------------product version----------------------------------");
        System.out.println(metaData.getDatabaseProductVersion());

        System.out.println("----------------------driver name---------------------------------");
        System.out.println(metaData.getDriverName());

        System.out.println("-----------------------driver version--------------------------------");
        System.out.println(metaData.getDriverVersion());

        System.out.println("-------------------------schemas------------------------------");
        System.out.println(stringify(provider.getSchemas(conn)));

        System.out.println("--------------------------catalogs-----------------------------");
        System.out.println(stringify(provider.getCatalogs(conn)));

        System.out.println("--------------------------databases-----------------------------");
        System.out.println(stringify(provider.getDatabases(conn)));

        System.out.println("--------------------------tables-----------------------------");
        System.out.println(stringify(provider.getTables(conn,"SPSV_DEV")));

        System.out.println("---------------------table info----------------------------------");
        System.out.println(stringify(provider.getTableInfo(conn,"SPSV_DEV","SPSV_ENTITY")));

        String sql = "SELECT a.* FROM ALL_COL_COMMENTS a\n" +
                "\tLEFT JOIN ALL_TAB_COLUMNS b ON a.OWNER = b.OWNER AND a.TABLE_NAME =b.TABLE_NAME AND a.COLUMN_NAME =b.COLUMN_NAME \n" +
                "\tWHERE a.OWNER LIKE ?\n" +
                "\tAND a.TABLE_NAME LIKE ?\n" +
                "\tORDER BY b.COLUMN_ID ASC ";

        PreparedStatement stat = conn.prepareStatement(sql);
        stat.setString(1, "SPSV_DEV");
        stat.setString(2, "SYS_USER");

        System.out.println("-------------------------------------------------------");
        System.out.println(stringify(JdbcResolver.parseResultSet(stat.executeQuery())));
        stat.close();


        conn.close();
    }


}
