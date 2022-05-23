package i2f.core.jdbc.sql.enums;

import i2f.core.jdbc.sql.consts.Sql;

/**
 * @author ltb
 * @date 2022/5/23 13:50
 * @desc
 */
public enum SqlJoin {
    INNER(Sql.INNER),
    LEFT(Sql.LEFT),
    RIGHT(Sql.RIGHT),
    OUTER(Sql.OUTER);
    private String type;
    private SqlJoin(String type){
        this.type=type;
    }
    public String type(){
        return this.type;
    }
}
