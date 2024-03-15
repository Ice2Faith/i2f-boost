package i2f.database.metadata.impl.postgresql;

import i2f.database.jdbc.data.StdType;

/**
 * @author Ice2Faith
 * @date 2024/3/14 16:02
 * @desc
 */
public enum PostgreSqlType {
    INTEGER("integer",false,false,StdType.INT),
    VARCHAR("varchar",true,false,StdType.VARCHAR),
    BIGINT("bigint",false,false,StdType.BIGINT),
    TIMESTAMP("timestamp",false,false,StdType.TIMESTAMP),
    DECIMAL("decimal",true,true,StdType.DECIMAL),
    CHAR("char",true,false,StdType.CHAR),
    DOUBLE("double",true,true,StdType.DOUBLE),
    FLOAT4("float4",true,true,StdType.FLOAT),
    FLOAT8("float8",true,true,StdType.DOUBLE),
    REAL("real",true,true,StdType.REAL),
    TEXT("text",false,false,StdType.TEXT),
    BYTEA("bytea",false,false,StdType.BLOB),
    TIMESTAMPTZ("timestamptz",false,false,StdType.TIMESTAMP),
    INT8("int8",false,false,StdType.BIGINT),
    BIT("bit",false,false,StdType.BIT),
    BOOL("bool",false,false,StdType.BOOL),
    DATE("date",false,false,StdType.DATE),
    TIME("time",false,false,StdType.TIME),
    TIMETZ("timetz",false,false,StdType.TIME),
    NUMERIC("numeric",true,true,StdType.NUMERIC),
    SMALLINT("smallint",false,false,StdType.SMALLINT),
    INT2("int2",false,false,StdType.SMALLINT),
    INT4("int4",false,false,StdType.INT),
    SMALLSERIAL("smallserial",false,false,StdType.SMALLINT),
    SERIAL("serial",false,false,StdType.INT),
    BIGSERIAL("bigserial",false,false,StdType.BIGINT),
    MONEY("money",false,false,StdType.DECIMAL),
    JSON("json",false,false,StdType.VARCHAR),
    INTERVAL("interval",false,false,StdType.VARCHAR),
    UUID("uuid",false,false,StdType.VARCHAR),
    POINT("point",false,false,StdType.VARCHAR),
    LSEG("lseg",false,false,StdType.VARCHAR),
    PATH("path",false,false,StdType.VARCHAR),
    BOX("box",false,false,StdType.VARCHAR),
    POLYGON("polygon",false,false,StdType.VARCHAR),
    LINE("line",false,false,StdType.VARCHAR),
    CIRCLE("circle",false,false,StdType.VARCHAR),
    JSONB("jsonb",false,false,StdType.BLOB),
    TIME_STAMP("time_stamp",false,false,StdType.TIMESTAMP),
    YES_OR_NO("yes_or_no",false,false,StdType.VARCHAR),
    XML("xml",false,false,StdType.VARCHAR),
    XID8("xid8",false,false,StdType.VARCHAR),
    MACADDR("macaddr",false,false,StdType.VARCHAR),
    INET("inet",false,false,StdType.VARCHAR),
    CIDR("cidr",false,false,StdType.VARCHAR),
    OID("oid",false,false,StdType.VARCHAR),
    BPCHAR("bpchar",false,false,StdType.VARCHAR),
    TID("tid",false,false,StdType.VARCHAR),
    XID("xid",false,false,StdType.VARCHAR),
    CID("cid",false,false,StdType.VARCHAR),
    MACADDR8("macaddr8",false,false,StdType.VARCHAR),
    VARBIT("varbit",false,false,StdType.VARCHAR),
    JSONPATH("jsonpath",false,false,StdType.VARCHAR),
    ;


    private String text;
    private boolean precision;
    private boolean scale;
    private StdType stdType;


    PostgreSqlType(String text, boolean precision, boolean scale, StdType stdType) {
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
