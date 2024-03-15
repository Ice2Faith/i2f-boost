package i2f.database.jdbc.data;

import java.util.List;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/14 11:41
 * @desc
 */
public class BindSql {
    protected boolean update;
    protected String sql;
    protected List<?> args;

    public BindSql() {
    }

    public BindSql(String sql, List<?> args) {
        this.sql = sql;
        this.args = args;
    }

    public BindSql(boolean update, String sql, List<?> args) {
        this.update = update;
        this.sql = sql;
        this.args = args;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<?> getArgs() {
        return args;
    }

    public void setArgs(List<?> args) {
        this.args = args;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BindSql bindSql = (BindSql) o;
        return update == bindSql.update &&
                Objects.equals(sql, bindSql.sql) &&
                Objects.equals(args, bindSql.args);
    }

    @Override
    public int hashCode() {
        return Objects.hash(update, sql, args);
    }

    @Override
    public String toString() {
        return "BindSql{" +
                "update=" + update +
                ", sql='" + sql + '\'' +
                ", args=" + args +
                '}';
    }
}
