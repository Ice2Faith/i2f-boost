package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.jdbc.sql.core.DbFinder;
import i2f.core.lambda.funcs.IBuilder;
import i2f.core.lambda.funcs.IGetter;
import i2f.core.lambda.funcs.ISetter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 15:59
 * @desc
 */
public class ColumnWrapper<N> implements INextWrapper<N> {
    protected N next;
    public List<String> cols=new ArrayList<>();
    protected String prefix=null;

    public ColumnWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }

    public ColumnWrapper<N> prefix(String prefix){
        this.prefix=prefix;
        return this;
    }

    public ColumnWrapper<N> col(String colName){
        if(prefix!=null){
            colName=prefix+colName;
        }
        cols.add(colName);
        return this;
    }
    public<T,R> ColumnWrapper<N> col(IGetter<T,R> getter){
        String colName = DbFinder.dbFieldName(getter);
        return col(colName);
    }
    public<T,V1> ColumnWrapper<N> col(ISetter<T,V1> setter){
        String colName = DbFinder.dbFieldName(setter);
        return col(colName);
    }
    public<T,R,V1> ColumnWrapper<N> col(IBuilder<T,R,V1> builder){
        String colName = DbFinder.dbFieldName(builder);
        return col(colName);
    }
    public ColumnWrapper<N> cols(String ... cols){
        for(String item : cols){
            col(item);
        }
        return this;
    }
}
