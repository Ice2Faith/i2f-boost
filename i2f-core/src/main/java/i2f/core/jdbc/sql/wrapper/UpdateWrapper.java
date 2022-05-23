package i2f.core.jdbc.sql.wrapper;

import i2f.core.jdbc.sql.wrapper.core.*;

/**
 * @author ltb
 * @date 2022/5/23 14:26
 * @desc
 */
public class UpdateWrapper
        implements IWhereWrapper<ConditionWrapper<UpdateWrapper>>,
        ITableWrapper<TableWrapper<UpdateWrapper>>,
        IBuildWrapper<UpdateWrapper>,IPreparable {
    protected TableWrapper<UpdateWrapper> table=new TableWrapper<>(this);
    protected ConditionWrapper<UpdateWrapper> condition=new ConditionWrapper<UpdateWrapper>(this);
    protected KeyValueWrapper<UpdateWrapper> kvs=new KeyValueWrapper<>(this);
    private UpdateWrapper(){

    }
    public static UpdateWrapper builder(){
        return new UpdateWrapper();
    }

    @Override
    public TableWrapper<UpdateWrapper> table() {
        return table;
    }

    public KeyValueWrapper<UpdateWrapper> sets(){
        return kvs;
    }

    @Override
    public ConditionWrapper<UpdateWrapper> where() {
        return condition;
    }


    @Override
    public UpdateWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        return null;
    }
}

