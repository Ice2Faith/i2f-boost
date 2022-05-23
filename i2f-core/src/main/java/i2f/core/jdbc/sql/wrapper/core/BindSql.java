package i2f.core.jdbc.sql.wrapper.core;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:34
 * @desc
 */
@Data
public class BindSql {
    public String sql;
    public List<Object> params=new ArrayList<>();
    public BindSql(String sql){
        this.sql=sql;
    }
    public BindSql(String sql,List<Object> params){
        this.sql=sql;
        this.params=params;
    }
}
