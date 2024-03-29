package i2f.core.database.jdbc.sql.wrapper.core;

import i2f.core.data.Pair;
import i2f.core.database.jdbc.sql.core.DbFinder;
import i2f.core.database.jdbc.sql.enums.SqlOrder;
import i2f.core.lang.functional.common.IBuilder;
import i2f.core.lang.functional.common.IGetter;
import i2f.core.lang.functional.common.ISetter;
import i2f.core.type.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 15:59
 * @desc
 */
public class OrderWrapper<N> implements INextWrapper<N> {
    protected N next;
    public List<Pair<String,String>> cols=new ArrayList<>();
    protected String prefix=null;
    protected String order= SqlOrder.ASC.sort();

    public OrderWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }

    public OrderWrapper<N> asc(){
        this.order=SqlOrder.ASC.sort();
        return this;
    }

    public OrderWrapper<N> desc(){
        this.order=SqlOrder.DESC.sort();
        return this;
    }

    public OrderWrapper<N> prefix(String prefix){
        this.prefix=prefix;
        return this;
    }

    public OrderWrapper<N> col(String colName) {
        if (prefix != null) {
            colName = prefix + colName;
        }
        cols.add(new Pair<>(colName, order));
        return this;
    }

    public <R, T> OrderWrapper<N> col(IGetter<R, T> getter) {
        String colName = DbFinder.dbFieldName(getter);
        return col(colName);
    }

    public <T, V1> OrderWrapper<N> col(ISetter<T, V1> setter) {
        String colName = DbFinder.dbFieldName(setter);
        return col(colName);
    }

    public <R, T, V1> OrderWrapper<N> col(IBuilder<R, T, V1> builder) {
        String colName = DbFinder.dbFieldName(builder);
        return col(colName);
    }

    public OrderWrapper<N> cols(String... cols) {
        for (String item : cols) {
            col(item);
        }
        return this;
    }

    public String orderColumns(){
        return Appender.builder()
                .addCollection(cols,", ",null,null,(Object val)->{
                    Pair<String,String> item=(Pair<String,String>)val;
                    return Appender.builder().addsSep(" ",item.key,item.val).get();
                }).get();
    }
}
