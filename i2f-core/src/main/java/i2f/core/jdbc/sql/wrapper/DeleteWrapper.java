package i2f.core.jdbc.sql.wrapper;

import i2f.core.jdbc.sql.wrapper.core.*;

/**
 * @author ltb
 * @date 2022/5/23 14:26
 * @desc
 */
public class DeleteWrapper
        implements IWhereWrapper<ConditionWrapper<DeleteWrapper>>,
        ITableWrapper<TableWrapper<DeleteWrapper>>,
        IBuildWrapper<DeleteWrapper>,
        IPreparable{
    protected TableWrapper<DeleteWrapper> table=new TableWrapper<>(this);
    protected ConditionWrapper<DeleteWrapper> condition=new ConditionWrapper<DeleteWrapper>(this);
    private DeleteWrapper(){

    }
    public static DeleteWrapper builder(){
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
        return null;
    }
}
