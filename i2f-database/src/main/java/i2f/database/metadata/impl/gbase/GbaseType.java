package i2f.database.metadata.impl.gbase;

import i2f.database.jdbc.data.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum GbaseType {
    INT("int",false,false,StdType.INT),
    VARCHAR("varchar",true,false,StdType.VARCHAR),
    BIGINT("bigint",false,false,StdType.BIGINT),
    DATETIME("datetime",false,false,StdType.DATETIME),
    DECIMAL("decimal",true,true,StdType.DECIMAL),
    CHAR("char",true,false,StdType.CHAR),
    DOUBLE("double",true,true,StdType.DOUBLE),
    FLOAT("float",true,true,StdType.FLOAT),
    REAL("real",true,true,StdType.REAL),
    TEXT("text",false,false,StdType.TEXT),
    BLOB("blob",false,false,StdType.BLOB),
    TIMESTAMP("timestamp",false,false,StdType.TIMESTAMP),
    INTEGER("integer",false,false,StdType.INTEGER),
    BIT("bit",false,false,StdType.BIT),
    BOOL("bool",false,false,StdType.BOOL),
    DATE("date",false,false,StdType.DATE),
    TIME("time",false,false,StdType.TIME),
    NUMERIC("numeric",true,true,StdType.NUMERIC),
    TINYINT("tinyint",false,false,StdType.TINYINT),
    SMALLINT("smallint",false,false,StdType.SMALLINT),
    MEDIUMINT("mediumint",false,false,StdType.MEDIUMINT),
    LONG("long",false,false,StdType.LONG),
    TINYTEXT("tinytext",false,false,StdType.TINYTEXT),
    MEDIUMTEXT("mediumtext",false,false,StdType.MEDIUMTEXT),
    LONGTEXT("longtext",false,false,StdType.LONGTEXT),
    TINYBLOB("tinyblob",false,false,StdType.TINYBLOB),
    MEDIUMBLOB("mediumblob",false,false,StdType.MEDIUMBLOB),
    LONGBLOB("longblob",false,false,StdType.LONGBLOB),
    BINARY("binary",false,false,StdType.BINARY),
    VARBINARY("varbinary",false,false,StdType.VARBINARY),
    ENUM("enum",false,false,StdType.ENUM),
    SET("set",false,false,StdType.SET),
    ;
    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;

    GbaseType(String text, boolean precision, boolean scale, StdType stdType) {
        this.text = text;
        this.precision = precision;
        this.scale = scale;
        this.stdType = stdType;
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

    public StdType stdType(){
        return stdType;
    }

}
