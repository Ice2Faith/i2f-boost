package i2f.core.jdbc.sql.wrapper;

import i2f.core.collection.CollectionUtil;
import i2f.core.data.Triple;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.wrapper.core.*;
import i2f.core.str.Appender;

import java.util.ArrayList;
import java.util.List;

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
        String sql = Appender.builder()
                .addsSep(" ", Sql.INSERT_INTO, table.fullTable())
                .line().add("(").line().tab()
                .addCollection(kvs.kvs, ",\n\t", null, null, (Object val) -> {
                    Triple<String, String, Object> item = (Triple<String, String, Object>) val;
                    return item.fst;
                })
                .line().add(")").line()
                .add(Sql.VALUES)
                .line().add("(").line().tab()
                .addCollection(kvs.kvs, ",\n\t", null, null, (Object val) -> {
                    return "?";
                })
                .line().add(")").line()
                .get();
        List<Object> params=new ArrayList<>();
        CollectionUtil.toCollection(params,kvs.kvs,0,-1,null,(Triple<String,String,Object> item)->{
            return item.trd;
        });
        return new BindSql(sql,params);
    }
}
