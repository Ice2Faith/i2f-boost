package i2f.database.jdbc.data;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Ice2Faith
 * @date 2024/3/14 11:04
 * @desc
 */
public class QueryResult {
    protected List<QueryColumn> columns;
    protected List<Map<String,Object>> rows;

    public List<QueryColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<QueryColumn> columns) {
        this.columns = columns;
    }

    public List<Map<String, Object>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        QueryResult that = (QueryResult) o;
        return Objects.equals(columns, that.columns) &&
                Objects.equals(rows, that.rows);
    }

    @Override
    public int hashCode() {
        return Objects.hash(columns, rows);
    }

    @Override
    public String toString() {
        return "QueryResult{" +
                "columns=" + columns +
                ", rows=" + rows +
                '}';
    }
}
