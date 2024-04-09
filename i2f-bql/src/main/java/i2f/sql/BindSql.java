package i2f.sql;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/4/7 16:52
 * @desc
 */
public class BindSql {
    public String sql;
    public List<Object> args = new ArrayList<>();

    public BindSql() {
    }

    public BindSql(String sql) {
        this.sql = sql;
    }

    public BindSql(String sql, List<Object> args) {
        this.sql = sql;
        this.args = args;
    }

    @Override
    public String toString() {
        return "Bql{\n" +
                "sql     = " + sql + "\n" +
                ", args  = " + args + "\n" +
                "}";
    }
}
