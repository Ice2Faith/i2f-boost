package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Triple;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.core.DbFinder;
import i2f.core.jdbc.sql.enums.SqlLink;
import i2f.core.lambda.funcs.IBuilder;
import i2f.core.lambda.funcs.IGetter;
import i2f.core.lambda.funcs.ISetter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 14:27
 * @desc
 */
public class ConditionWrapper<N>
        implements INextWrapper<N> {
    protected N next;
    public List<Triple<String,String,Object>> conditions=new ArrayList<>();
    public SqlLink link=SqlLink.AND;
    protected String prefix;
    public ConditionWrapper(N next){
        this.next=next;
    }

    public ConditionWrapper<N> and(){
        link=SqlLink.AND;
        return this;
    }
    public ConditionWrapper<N> or(){
        link=SqlLink.OR;
        return this;
    }

    public ConditionWrapper<N> prefix(String prefix){
        this.prefix=prefix;
        return this;
    }
    @Override
    public N next(){
        return next;
    }
    /////////////////////////////////////////////////////////////////////////////////
    public ConditionWrapper<N> cond(boolean cond,String name,String ope,Object val){
        if(!cond){
            return this;
        }
        if(prefix!=null){
            name=prefix+name;
        }
        conditions.add(new Triple<>(name,ope,val));
        return this;
    }
    public<T,R> ConditionWrapper<N> cond(boolean cond, IGetter<T,R> getter, String ope, Object val){
        String name = DbFinder.dbFieldName(getter);
        return cond(cond,name,ope,val);
    }
    public<T,V1> ConditionWrapper<N> cond(boolean cond, ISetter<T,V1> setter, String ope, Object val){
        String name = DbFinder.dbFieldName(setter);
        return cond(cond,name,ope,val);
    }
    public<T,R,V1> ConditionWrapper<N> cond(boolean cond, IBuilder<T,R,V1> builder, String ope, Object val){
        String name = DbFinder.dbFieldName(builder);
        return cond(cond,name,ope,val);
    }
    ///////////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq(boolean cond, String col,Object val){
        return cond(cond,col, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq(boolean cond, String col,Object val){
        return cond(cond,col, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt(boolean cond, String col,Object val){
        return cond(cond,col, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt(boolean cond, String col,Object val){
        return cond(cond,col, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte(boolean cond, String col,Object val){
        return cond(cond,col, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte(boolean cond, String col,Object val){
        return cond(cond,col, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in(boolean cond, String col,Object val){
        return cond(cond,col, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like(boolean cond, String col,Object val){
        return cond(cond,col, Sql.LIKE,val);
    }
    //////////////////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like(boolean cond, IGetter<T,R> getter,Object val){
        return cond(cond,getter, Sql.LIKE,val);
    }
    /////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like(boolean cond, ISetter<T,R> setter,Object val){
        return cond(cond,setter, Sql.LIKE,val);
    }
    /////////////////////////////////////////////////////////////



    public ConditionWrapper<N> cond(String name,String ope,Object val){
        return cond(true,name,ope,val);
    }
    public<T,R> ConditionWrapper<N> cond( IGetter<T,R> getter, String ope, Object val){
        String name = DbFinder.dbFieldName(getter);
        return cond(true,name,ope,val);
    }
    public<T,V1> ConditionWrapper<N> cond( ISetter<T,V1> setter, String ope, Object val){
        String name = DbFinder.dbFieldName(setter);
        return cond(true,name,ope,val);
    }
    public<T,R,V1> ConditionWrapper<N> cond( IBuilder<T,R,V1> builder, String ope, Object val){
        String name = DbFinder.dbFieldName(builder);
        return cond(true,name,ope,val);
    }
    ///////////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq( String col,Object val){
        return cond(true,col, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq( String col,Object val){
        return cond(true,col, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt( String col,Object val){
        return cond(true,col, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt( String col,Object val){
        return cond(true,col, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte( String col,Object val){
        return cond(true,col, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte( String col,Object val){
        return cond(true,col, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in( String col,Object val){
        return cond(true,col, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like( String col,Object val){
        return cond(true,col, Sql.LIKE,val);
    }
    //////////////////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like( IGetter<T,R> getter,Object val){
        return cond(true,getter, Sql.LIKE,val);
    }
    /////////////////////////////////////////////////
    public<T,R> ConditionWrapper<N> eq( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.EQ,val);
    }
    public<T,R> ConditionWrapper<N> neq( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.NEQ,val);
    }
    public<T,R> ConditionWrapper<N> gt( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.GT,val);
    }
    public<T,R> ConditionWrapper<N> lt( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.LT,val);
    }
    public<T,R> ConditionWrapper<N> gte( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.GTE,val);
    }
    public<T,R> ConditionWrapper<N> lte( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.LTE,val);
    }
    public<T,R> ConditionWrapper<N> in( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like( ISetter<T,R> setter,Object val){
        return cond(true,setter, Sql.LIKE,val);
    }
}
