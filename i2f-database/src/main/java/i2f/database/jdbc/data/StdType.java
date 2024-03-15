package i2f.database.jdbc.data;

import java.math.BigDecimal;
import java.sql.JDBCType;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum StdType {
    INT("int",false,false,Integer.class,JDBCType.INTEGER,Integer.class,JDBCType.NUMERIC),
    VARCHAR("varchar",true,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    BIGINT("bigint",false,false,Long.class,JDBCType.BIGINT,Long.class,JDBCType.NUMERIC),
    DATETIME("datetime",false,false,Date.class,JDBCType.DATE,Date.class,JDBCType.DATE),
    DECIMAL("decimal",true,true,BigDecimal.class,JDBCType.DECIMAL,BigDecimal.class,JDBCType.NUMERIC),
    CHAR("char",true,false,String.class,JDBCType.CHAR,String.class,JDBCType.VARCHAR),
    DOUBLE("double",true,true,Double.class,JDBCType.DOUBLE,Double.class,JDBCType.NUMERIC),
    FLOAT("float",true,true,Float.class,JDBCType.FLOAT,Double.class,JDBCType.NUMERIC),
    REAL("real",true,true,Double.class,JDBCType.REAL,Double.class,JDBCType.NUMERIC),
    TEXT("text",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    BLOB("blob",false,false,String.class,JDBCType.BLOB,String.class,JDBCType.BLOB),
    TIMESTAMP("timestamp",false,false,Timestamp.class,JDBCType.TIMESTAMP,Date.class,JDBCType.DATE),
    INTEGER("integer",false,false,Integer.class,JDBCType.INTEGER,Integer.class,JDBCType.NUMERIC),
    BIT("bit",false,false,Boolean.class,JDBCType.BIT,Boolean.class,JDBCType.BIT),
    BOOL("bool",false,false,Boolean.class,JDBCType.BOOLEAN,Boolean.class,JDBCType.BOOLEAN),
    DATE("date",false,false,Date.class,JDBCType.DATE,Date.class,JDBCType.DATE),
    TIME("time",false,false,Date.class,JDBCType.TIME,Date.class,JDBCType.TIME),
    NUMERIC("numeric",true,true,Double.class,JDBCType.NUMERIC,Double.class,JDBCType.NUMERIC),
    TINYINT("tinyint",false,false,Short.class,JDBCType.TINYINT,Integer.class,JDBCType.NUMERIC),
    SMALLINT("smallint",false,false,Short.class,JDBCType.SMALLINT,Integer.class,JDBCType.NUMERIC),
    MEDIUMINT("mediumint",false,false,Integer.class,JDBCType.INTEGER,Integer.class,JDBCType.NUMERIC),
    LONG("long",false,false,Long.class,JDBCType.BIGINT,Long.class,JDBCType.NUMERIC),
    TINYTEXT("tinytext",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    MEDIUMTEXT("mediumtext",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    LONGTEXT("longtext",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    TINYBLOB("tinyblob",false,false,String.class,JDBCType.BLOB,String.class,JDBCType.VARCHAR),
    MEDIUMBLOB("mediumblob",false,false,String.class,JDBCType.BLOB,String.class,JDBCType.VARCHAR),
    LONGBLOB("longblob",false,false,String.class,JDBCType.BLOB,String.class,JDBCType.VARCHAR),
    BINARY("binary",false,false,String.class,JDBCType.BINARY,String.class,JDBCType.VARCHAR),
    VARBINARY("varbinary",false,false,String.class,JDBCType.VARBINARY,String.class,JDBCType.VARCHAR),
    YEAR("year",false,false,Integer.class,JDBCType.NUMERIC,String.class,JDBCType.VARCHAR),
    ENUM("enum",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    SET("set",false,false,String.class,JDBCType.VARCHAR,String.class,JDBCType.VARCHAR),
    ;
    private String text;
    private boolean precision;
    private boolean scale;
    private Class<?> javaType;
    private JDBCType jdbcType;
    private Class<?> looseJavaType;
    private JDBCType looseJdbcType;

    private StdType(String text, boolean precision, boolean scale, Class<?> javaType, JDBCType jdbcType, Class<?> looseJavaType, JDBCType looseJdbcType) {
        this.text = text;
        this.precision = precision;
        this.scale = scale;
        this.javaType = javaType;
        this.jdbcType = jdbcType;
        this.looseJavaType = looseJavaType;
        this.looseJdbcType = looseJdbcType;
    }

    public String text() {
        return text;
    }

    public boolean precision() {
        return precision;
    }

    public boolean scale() {
        return scale;
    }

    public Class<?> javaType(){
        return javaType;
    }

    public JDBCType jdbcType(){
        return jdbcType;
    }

    public Class<?> looseJavaType() {
        return looseJavaType;
    }

    public JDBCType looseJdbcType() {
        return looseJdbcType;
    }
}
