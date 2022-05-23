package i2f.core.jdbc.sql.enums;

import i2f.core.jdbc.sql.consts.Sql;

/**
 * @author ltb
 * @date 2022/5/23 13:52
 * @desc
 */
public enum SqlOrder {
    ASC(Sql.ASC),
    DESC(Sql.DESC);
    private String sort;
    private SqlOrder(String sort){
        this.sort=sort;
    }
    public String sort(){
        return this.sort;
    }

}
