package i2f.core.database.jdbc.sql.wrapper;

import i2f.core.database.jdbc.sql.wrapper.core.*;

/**
 * @author ltb
 * @date 2022/5/23 14:25
 * @desc
 */
public class InsertBatchWrapper
        implements ITableWrapper<TableWrapper<InsertBatchWrapper>>,
        IBuildWrapper<InsertBatchWrapper>,
        IPreparable {
    protected TableWrapper<InsertBatchWrapper> table=new TableWrapper<>(this);
    private InsertBatchWrapper(){

    }
    public static InsertBatchWrapper builder(){
        return new InsertBatchWrapper();
    }

    @Override
    public TableWrapper<InsertBatchWrapper> table() {
        return table;
    }

    @Override
    public InsertBatchWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        return null;
    }
}
