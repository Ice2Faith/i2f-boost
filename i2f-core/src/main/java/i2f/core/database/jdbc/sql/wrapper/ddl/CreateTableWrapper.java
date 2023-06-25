package i2f.core.database.jdbc.sql.wrapper.ddl;

import i2f.core.database.jdbc.sql.consts.Sql;
import i2f.core.database.jdbc.sql.wrapper.core.*;
import i2f.core.type.str.Appender;

/**
 * @author ltb
 * @date 2022/5/23 14:24
 * @desc
 */
public class CreateTableWrapper
        implements ITableWrapper<TableWrapper<CreateTableWrapper>>,
        IBuildWrapper<CreateTableWrapper>,
        IPreparable {
    protected TableWrapper<CreateTableWrapper> table=new TableWrapper<>(this);
    protected DdlColumnWrapper<CreateTableWrapper> columns=new DdlColumnWrapper<>(this);
    private CreateTableWrapper(){

    }
    public static CreateTableWrapper builder(){
        return new CreateTableWrapper();
    }

    @Override
    public TableWrapper<CreateTableWrapper> table() {
        return table;
    }

    public DdlColumnWrapper<CreateTableWrapper> columns(){
        return columns;
    }

    @Override
    public CreateTableWrapper done() {
        return this;
    }

    @Override
    public BindSql prepare() {
        Appender<StringBuilder> builder = Appender.builder()
                .addsSep(" ", Sql.CREATE_TABLE, table.fullTable(), "(","\n\t")
                .add(columns.columnDefines())
                .add("\n);");
        String sql= builder.get();
        return new BindSql(sql);
    }
}
