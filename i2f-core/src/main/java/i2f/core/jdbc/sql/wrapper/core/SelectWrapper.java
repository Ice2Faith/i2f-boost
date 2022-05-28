package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Pair;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.core.DbFinder;
import i2f.core.lambda.Lambdas;
import i2f.core.lambda.funcs.IBuilder;
import i2f.core.lambda.funcs.IGetter;
import i2f.core.lambda.funcs.ISetter;
import i2f.core.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 15:59
 * @desc
 */
public class SelectWrapper<N> implements INextWrapper<N> {
    protected N next;
    public boolean distinct=false;
    public List<Pair<String,String>> cols=new ArrayList<>();
    protected String prefix=null;

    public SelectWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }

    public SelectWrapper<N> prefix(String prefix){
        this.prefix=prefix;
        return this;
    }

    public SelectWrapper<N> distinct(){
        this.distinct=true;
        return this;
    }

    public SelectWrapper<N> col(String colName,String colAs){
        if(prefix!=null){
            colName=prefix+colName;
        }
        cols.add(new Pair<>(colName,colAs));
        return this;
    }
    public SelectWrapper<N> col(String colName){
        return col(colName,colName);
    }
    public<T,R> SelectWrapper<N> col(IGetter<T,R> getter){
        String colName = DbFinder.dbFieldName(getter);
        String fieldName= Lambdas.fieldName(getter);
        return col(colName,fieldName);
    }
    public<T,V1> SelectWrapper<N> col(ISetter<T,V1> setter){
        String colName = DbFinder.dbFieldName(setter);
        String fieldName= Lambdas.fieldName(setter);
        return col(colName,fieldName);
    }
    public<T,R,V1> SelectWrapper<N> col(IBuilder<T,R,V1> builder){
        String colName = DbFinder.dbFieldName(builder);
        String fieldName= Lambdas.fieldName(builder);
        return col(colName,fieldName);
    }
    public SelectWrapper<N> cols(String ... cols){
        for(String item : cols){
            col(item);
        }
        return this;
    }

    public String selectColumns(){
        return Appender.builder()
                .addWhen(distinct, Sql.DISTINCT)
                .blank().addCollection(cols,",\n\t",null,null,(Object val)->{
            Pair<String, String> item=(Pair<String,String>) val;
            return Appender.builder().addsSep(" ",item.key,Sql.AS,item.val).get();
        }).get();
    }
}
