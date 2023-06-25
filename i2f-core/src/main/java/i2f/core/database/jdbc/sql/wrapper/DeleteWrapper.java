package i2f.core.database.jdbc.sql.wrapper;

import i2f.core.database.jdbc.sql.consts.Sql;
import i2f.core.database.jdbc.sql.wrapper.core.*;
import i2f.core.type.str.Appender;

import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 14:26
 * @desc
 */
public class DeleteWrapper
        implements IWhereWrapper<ConditionWrapper<DeleteWrapper>>,
        ITableWrapper<TableWrapper<DeleteWrapper>>,
        IBuildWrapper<DeleteWrapper>,
        IPreparable {
    protected TableWrapper<DeleteWrapper> table = new TableWrapper<>(this);
    protected ConditionWrapper<DeleteWrapper> condition = new ConditionWrapper<DeleteWrapper>(this);

    private DeleteWrapper() {

    }

    public static DeleteWrapper builder() {
        return new DeleteWrapper();
    }

    @Override
    public TableWrapper<DeleteWrapper> table() {
        return table;
    }

    @Override
    public ConditionWrapper<DeleteWrapper> where() {
        return condition;
    }

    @Override
    public DeleteWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        BindSql condBindSql=condition.prepare();
        Appender<StringBuilder> builder = Appender.builder().addsSep(" ", Sql.DELETE_FORM, table.fullTable());
        if(condBindSql.sql!=null && !"".equals(condBindSql.sql)){
            builder.line()
                    .add(Sql.WHERE)
                    .line()
                    .add(condBindSql.sql);
        }
        String sql=builder.get();
        List<Object> params=condBindSql.params;
        return new BindSql(sql,params);
    }
}
