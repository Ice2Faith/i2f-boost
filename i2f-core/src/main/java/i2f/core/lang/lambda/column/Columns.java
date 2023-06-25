package i2f.core.lang.lambda.column;

import i2f.core.lang.functional.common.IGetter;
import i2f.core.lang.lambda.Lambdas;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/9/1 10:30
 * @desc
 */
public class Columns {
    public static Columns select() {
        return new Columns();
    }

    //////////////////////////////////////////////////////////////////////////////
    protected Object prefix = null;
    protected List<Column> columns = new LinkedList<>();

    //////////////////////////////////////////////////////////////////////////////
    public Columns() {

    }

    //////////////////////////////////////////////////////////////////////////////
    public Columns prefix(Object prefix) {
        this.prefix = prefix;
        return this;
    }

    public Columns noPrefix() {
        this.prefix = null;
        return this;
    }

    //////////////////////////////////////////////////////////////////////////////
    public Columns col(boolean decide, Column col) {
        if (col == null) {
            return this;
        }
        if (decide) {
            this.columns.add(col);
        }
        return this;
    }

    public Columns col(boolean decide, Object prefix, Object column, Object alias) {
        if (decide) {
            Column col = new Column(prefix, column, alias);
            this.columns.add(col);
        }
        return this;
    }

    public Columns col(boolean decide, Object column, Object alias) {
        return col(decide, this.prefix, column, alias);
    }

    public Columns col(boolean decide, Object column) {
        return col(decide, this.prefix, column, null);
    }

    public <R, T> Columns col(boolean decide, Object prefix, IGetter<R, T> column, Object alias) {
        String columnName = Lambdas.fieldName(column);
        return col(decide, prefix, columnName, alias);
    }

    public <R, T> Columns col(boolean decide, IGetter<R, T> column, Object alias) {
        String columnName = Lambdas.fieldName(column);
        return col(decide, columnName, alias);
    }

    public <R, T> Columns col(boolean decide, IGetter<R, T> column) {
        String columnName = Lambdas.fieldName(column);
        return col(decide, columnName);
    }

    //////////////////////////////////////////////////////////////////////////////
    public Columns col(Column col) {
        return col(true, col);
    }

    public Columns col(Object prefix, Object column, Object alias) {
        return col(true, prefix, column, alias);
    }

    public Columns col(Object column, Object alias) {
        return col(true, column, alias);
    }

    public Columns col(Object column) {
        return col(true, column);
    }

    public <R, T> Columns col(Object prefix, IGetter<R, T> column, Object alias) {
        return col(true, prefix, column, alias);
    }

    public <R, T> Columns col(IGetter<R, T> column, Object alias) {
        return col(true, column, alias);
    }

    public <R, T> Columns col(IGetter<R, T> column) {
        return col(true, column);
    }

    //////////////////////////////////////////////////////////////////////////////
    public Columns cols(boolean decide, Object prefix, Object... columns) {
        for (Object item : columns) {
            col(decide, prefix, item, null);
        }
        return this;
    }

    public Columns cols(boolean decide, Object... columns) {
        for (Object item : columns) {
            col(decide, item);
        }
        return this;
    }

    public Columns cols(boolean decide, Object prefix, Collection<?> columns) {
        for (Object item : columns) {
            col(decide, prefix, item, null);
        }
        return this;
    }

    public Columns cols(boolean decide, Collection<?> columns) {
        for (Object item : columns) {
            col(decide, item);
        }
        return this;
    }

    public <R, T> Columns cols(boolean decide, Object prefix, IGetter<R, T>... columns) {
        for (IGetter<R, T> item : columns) {
            col(decide, prefix, item, null);
        }
        return this;
    }

    public <R, T> Columns cols(boolean decide, IGetter<R, T>... columns) {
        for (IGetter<R, T> item : columns) {
            col(decide, item);
        }
        return this;
    }


    public Columns cols(Object prefix, Object... columns) {
        return cols(true, prefix, columns);
    }

    public Columns cols(Object... columns) {
        return cols(true, columns);
    }

    public Columns cols(Object prefix, Collection<?> columns) {
        return cols(true, prefix, columns);
    }

    public Columns cols(Collection<?> columns) {
        return cols(true, columns);
    }

    public <R, T> Columns cols(Object prefix, IGetter<R, T>... columns) {
        return cols(true, prefix, columns);
    }

    public <R, T> Columns cols(IGetter<R, T>... columns) {
        return cols(true, columns);
    }
}
