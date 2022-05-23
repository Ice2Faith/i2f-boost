package i2f.core.jdbc.sql.enums;

import i2f.core.jdbc.sql.consts.Sql;

/**
 * @author ltb
 * @date 2022/5/23 13:51
 * @desc
 */
public enum  SqlLink {
    AND(Sql.AND),
    OR(Sql.OR);
    private String linker;
    private SqlLink(String linker){
        this.linker=linker;
    }
    public String linker(){
        return this.linker;
    }

}
