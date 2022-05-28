package i2f.core.jdbc.sql.wrapper.ddl;

import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.wrapper.core.*;
import i2f.core.str.Appender;

/**
 * @author ltb
 * @date 2022/5/23 14:24
 * @desc
 */
public class DropTableWrapper
        implements ITableWrapper<TableWrapper<DropTableWrapper>>,
        IBuildWrapper<DropTableWrapper>,
        IPreparable {
    protected TableWrapper<DropTableWrapper> table=new TableWrapper<>(this);
    private DropTableWrapper(){

    }
    public static DropTableWrapper builder(){
        return new DropTableWrapper();
    }

    @Override
    public TableWrapper<DropTableWrapper> table() {
        return table;
    }

    @Override
    public DropTableWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        String sql = Appender.builder()
                .addsSep(" ",Sql.DROP_TABLE,table.fullTable(),";")
                .get();
        return new BindSql(sql);
    }
}
