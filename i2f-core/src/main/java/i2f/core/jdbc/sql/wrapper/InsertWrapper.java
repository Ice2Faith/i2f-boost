package i2f.core.jdbc.sql.wrapper;

import i2f.core.jdbc.sql.wrapper.core.*;

/**
 * @author ltb
 * @date 2022/5/23 14:25
 * @desc
 */
public class InsertWrapper
        implements ITableWrapper<TableWrapper<InsertWrapper>>,
        IBuildWrapper<InsertWrapper>,
        IPreparable {
    protected TableWrapper<InsertWrapper> table=new TableWrapper<>(this);
    protected KeyValueWrapper<InsertWrapper> kvs=new KeyValueWrapper<>(this);
    private InsertWrapper(){

    }
    public static InsertWrapper builder(){
        return new InsertWrapper();
    }

    @Override
    public TableWrapper<InsertWrapper> table() {
        return table;
    }

    public KeyValueWrapper<InsertWrapper> values(){
        return kvs;
    }

    @Override
    public InsertWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        return null;
    }
}
