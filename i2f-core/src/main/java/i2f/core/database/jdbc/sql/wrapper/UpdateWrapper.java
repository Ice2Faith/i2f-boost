package i2f.core.database.jdbc.sql.wrapper;

import i2f.core.container.collection.CollectionUtil;
import i2f.core.data.Triple;
import i2f.core.database.jdbc.sql.consts.Sql;
import i2f.core.database.jdbc.sql.wrapper.core.*;
import i2f.core.type.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 14:26
 * @desc
 */
public class UpdateWrapper
        implements IWhereWrapper<ConditionWrapper<UpdateWrapper>>,
        ITableWrapper<TableWrapper<UpdateWrapper>>,
        IBuildWrapper<UpdateWrapper>, IPreparable {
    protected TableWrapper<UpdateWrapper> table = new TableWrapper<>(this);
    protected ConditionWrapper<UpdateWrapper> condition = new ConditionWrapper<UpdateWrapper>(this);
    protected KeyValueWrapper<UpdateWrapper> kvs = new KeyValueWrapper<>(this);

    private UpdateWrapper() {

    }

    public static UpdateWrapper builder() {
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
        BindSql condBindSql=condition.prepare();
        Appender<StringBuilder> builder = Appender.builder()
                .addsSep(" ", Sql.UPDATE, table.fullTable())
                .line().add(Sql.SET).line().tab()
                .addCollection(kvs.kvs, ",\n\t", null, null, (Object val) -> {
                    Triple<String, String, Object> item = (Triple<String, String, Object>) val;
                    return Appender.builder().addsSep(" ", item.fst, item.sec, "?").get();
                });

        List<Object> params=new ArrayList<>();
        CollectionUtil.collect(params, kvs.kvs, 0, -1, null, (Triple<String, String, Object> item) -> {
            return item.trd;
        });

        if(condBindSql.sql != null && !"".equals(condBindSql.sql)){
            builder.line()
                    .add(Sql.WHERE)
                    .line()
                    .add(condBindSql.sql);
            params.addAll(condBindSql.params);
        }
        String sql = builder.get();

        return new BindSql(sql,params);
    }
}

