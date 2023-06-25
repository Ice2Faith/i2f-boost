package i2f.core.database.jdbc.sql.wrapper.core;

import i2f.core.container.collection.Collections;
import i2f.core.database.jdbc.sql.consts.Sql;
import i2f.core.database.jdbc.sql.core.DbFinder;
import i2f.core.database.jdbc.sql.enums.SqlLink;
import i2f.core.lang.functional.common.IBuilder;
import i2f.core.lang.functional.common.IGetter;
import i2f.core.lang.functional.common.ISetter;
import i2f.core.type.str.Appender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 14:27
 * @desc
 */
public class ConditionWrapper<N>
        implements INextWrapper<N>,
        IPreparable{
    protected N next;
    public List<BindSql> conditions=new ArrayList<>();
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
    public ConditionWrapper<N> condFree(boolean cond,String sql,Object ... params){
        BindSql bind = new BindSql(sql, Collections.arrayList(params));
        return cond(cond,bind);
    }
    public ConditionWrapper<N> cond(boolean cond,BindSql bind){
        if(!cond){
            return this;
        }
        conditions.add(bind);
        return this;
    }
    public ConditionWrapper<N> cond(boolean cond,String name,String ope,Object val){
        if(!cond){
            return this;
        }
        if(prefix!=null){
            name=prefix+name;
        }
        Appender<StringBuilder> builder = Appender.builder()
                .addsSep(" ", name, ope)
                .blank();
        List<Object> params=new ArrayList<>();

        if(Sql.LIKE.equalsIgnoreCase(ope)){
            builder.add("concat('%',concat(?,'%'))");
            params.add(val);
        }else if(val instanceof Iterator){
            Iterator iterator=(Iterator)val;
            List<String> holdSpace=new ArrayList<>();
            while(iterator.hasNext()){
                Object item=iterator.next();
                holdSpace.add("?");
                params.add(item);
            }
            builder.addCollection(holdSpace,",","(",")");
        }else{
            builder.add("?");
            params.add(val);
        }

        String sql = builder.get();

        BindSql bind = new BindSql(sql, params);
        conditions.add(bind);
        return this;
    }

    public <R, T> ConditionWrapper<N> cond(boolean cond, IGetter<R, T> getter, String ope, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return cond(cond, name, ope, val);
    }

    public <T, V1> ConditionWrapper<N> cond(boolean cond, ISetter<T, V1> setter, String ope, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return cond(cond, name, ope, val);
    }

    public <R, T, V1> ConditionWrapper<N> cond(boolean cond, IBuilder<R, T, V1> builder, String ope, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return cond(cond, name, ope, val);
    }

    ///////////////////////////////////////////////////////
    public <T, R> ConditionWrapper<N> eq(boolean cond, String col, Object val) {
        return cond(cond, col, Sql.EQ, val);
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
    public<T,R> ConditionWrapper<N> in(boolean cond, String col, Iterator<?> val){
        return cond(cond,col, Sql.IN,val);
    }
    public<T,R> ConditionWrapper<N> like(boolean cond, String col,Object val){
        return cond(cond,col, Sql.LIKE,val);
    }
    public<T,R> ConditionWrapper<N> likes(boolean cond, String col,Iterator<?> val){
        if(!cond){
            return this;
        }
        if(prefix!=null){
            col=prefix+col;
        }
        Appender<StringBuilder> builder = Appender.builder()
                .add("(")
                .blank();
        List<Object> params=new ArrayList<>();

        List<String> parts=new ArrayList<>();
        while(val.hasNext()){
            Object item=val.next();
            parts.add(Appender.builder().addsSep(" ",col,Sql.LIKE,"concat('%',concat(?,'%'))").get());
            params.add(item);
        }

        String sql= builder.addCollection(parts,"\n\tor ")
                .line()
                .add(")")
                .get();

        BindSql bind=new BindSql(sql,params);
        conditions.add(bind);
        return this;
    }
    public<T,R> ConditionWrapper<N> multiLike(boolean cond,Iterator<String> cols,Object val){
        if(!cond){
            return this;
        }

        Appender<StringBuilder> builder = Appender.builder()
                .add("(")
                .blank();
        List<Object> params=new ArrayList<>();

        List<String> parts=new ArrayList<>();
        while(cols.hasNext()){
            String col=cols.next();
            if(prefix!=null){
                col=prefix+col;
            }
            parts.add(Appender.builder().addsSep(" ",col,Sql.LIKE,"concat('%',concat(?,'%'))").get());
            params.add(val);
        }

        String sql= builder.addCollection(parts,"\n\tor ")
                .line()
                .add(")")
                .get();

        BindSql bind=new BindSql(sql,params);
        conditions.add(bind);
        return this;
    }

    //////////////////////////////////////////////////////////////
    public <R, T> ConditionWrapper<N> eq(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.EQ, val);
    }

    public <R, T> ConditionWrapper<N> neq(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.NEQ, val);
    }

    public <R, T> ConditionWrapper<N> gt(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.GT, val);
    }

    public <R, T> ConditionWrapper<N> lt(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.LT, val);
    }

    public <R, T> ConditionWrapper<N> gte(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.GTE, val);
    }

    public <R, T> ConditionWrapper<N> lte(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.LTE, val);
    }

    public <R, T> ConditionWrapper<N> in(boolean cond, IGetter<R, T> getter, Iterator<?> val) {
        return cond(cond, getter, Sql.IN, val);
    }

    public <R, T> ConditionWrapper<N> like(boolean cond, IGetter<R, T> getter, Object val) {
        return cond(cond, getter, Sql.LIKE, val);
    }

    public <R, T> ConditionWrapper<N> likes(boolean cond, IGetter<R, T> getter, Iterator<?> val) {
        String name = DbFinder.dbFieldName(getter);
        return likes(cond, name, val);
    }

    public ConditionWrapper<N> multiLikeGetter(boolean cond, Iterator<IGetter> getters, Object val) {
        List<String> cols = new ArrayList<>();
        while (getters.hasNext()) {
            IGetter item = getters.next();
            String name = DbFinder.dbFieldName(item);
            cols.add(name);
        }
        return multiLike(cond, cols.iterator(), val);
    }

    /////////////////////////////////////////////////
    public <T, V1> ConditionWrapper<N> eq(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.EQ, val);
    }

    public <T, V1> ConditionWrapper<N> neq(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.NEQ, val);
    }

    public <T, V1> ConditionWrapper<N> gt(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.GT, val);
    }

    public <T, V1> ConditionWrapper<N> lt(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.LT, val);
    }

    public <T, V1> ConditionWrapper<N> gte(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.GTE, val);
    }

    public <T, V1> ConditionWrapper<N> lte(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.LTE, val);
    }

    public <T, V1> ConditionWrapper<N> in(boolean cond, ISetter<T, V1> setter, Iterator<?> val) {
        return cond(cond, setter, Sql.IN, val);
    }

    public <T, V1> ConditionWrapper<N> like(boolean cond, ISetter<T, V1> setter, Object val) {
        return cond(cond, setter, Sql.LIKE, val);
    }

    public <T, V1> ConditionWrapper<N> likes(boolean cond, ISetter<T, V1> setter, Iterator<?> val) {
        String name = DbFinder.dbFieldName(setter);
        return likes(cond, name, val);
    }

    public ConditionWrapper<N> multiLikeSetter(boolean cond, Iterator<ISetter> setters, Object val) {
        List<String> cols = new ArrayList<>();
        while (setters.hasNext()) {
            ISetter item = setters.next();
            String name = DbFinder.dbFieldName(item);
            cols.add(name);
        }
        return multiLike(cond, cols.iterator(), val);
    }

    /////////////////////////////////////////////////////////////
    public ConditionWrapper<N> condFree(String sql, Object... params) {
        return condFree(true,sql,params);
    }
    public ConditionWrapper<N> cond(BindSql bind){
        return cond(true,bind);
    }

    public ConditionWrapper<N> cond(String name, String ope, Object val) {
        return cond(true, name, ope, val);
    }

    public <R, T> ConditionWrapper<N> cond(IGetter<R, T> getter, String ope, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return cond(true, name, ope, val);
    }

    public <T, V1> ConditionWrapper<N> cond(ISetter<T, V1> setter, String ope, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return cond(true, name, ope, val);
    }

    public <R, T, V1> ConditionWrapper<N> cond(IBuilder<R, T, V1> builder, String ope, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return cond(true, name, ope, val);
    }

    ///////////////////////////////////////////////////////
    public ConditionWrapper<N> eq(String col, Object val) {
        return cond(true, col, Sql.EQ, val);
    }

    public ConditionWrapper<N> neq(String col, Object val) {
        return cond(true, col, Sql.NEQ, val);
    }

    public ConditionWrapper<N> gt(String col, Object val) {
        return cond(true, col, Sql.GT, val);
    }

    public ConditionWrapper<N> lt(String col, Object val) {
        return cond(true, col, Sql.LT, val);
    }

    public ConditionWrapper<N> gte(String col, Object val) {
        return cond(true, col, Sql.GTE, val);
    }

    public ConditionWrapper<N> lte(String col, Object val) {
        return cond(true, col, Sql.LTE, val);
    }

    public ConditionWrapper<N> in(String col, Iterator<?> val) {
        return cond(true, col, Sql.IN, val);
    }

    public ConditionWrapper<N> like(String col, Object val) {
        return cond(true, col, Sql.LIKE, val);
    }

    public ConditionWrapper<N> likes(String col, Iterator<?> val) {
        return likes(true, col, val);
    }

    public ConditionWrapper<N> multiLike(Iterator<String> cols, Object val) {
        return multiLike(true, cols, val);
    }

    //////////////////////////////////////////////////////////////
    public <R, T> ConditionWrapper<N> eq(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.EQ, val);
    }

    public <R, T> ConditionWrapper<N> neq(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.NEQ, val);
    }

    public <R, T> ConditionWrapper<N> gt(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.GT, val);
    }

    public <R, T> ConditionWrapper<N> lt(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.LT, val);
    }

    public <R, T> ConditionWrapper<N> gte(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.GTE, val);
    }

    public <R, T> ConditionWrapper<N> lte(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.LTE, val);
    }

    public <R, T> ConditionWrapper<N> in(IGetter<R, T> getter, Iterator<?> val) {
        return cond(true, getter, Sql.IN, val);
    }

    public <R, T> ConditionWrapper<N> like(IGetter<R, T> getter, Object val) {
        return cond(true, getter, Sql.LIKE, val);
    }

    public <R, T> ConditionWrapper<N> likes(IGetter<R, T> getter, Iterator<?> val) {
        String name = DbFinder.dbFieldName(getter);
        return likes(true, name, val);
    }

    public ConditionWrapper<N> multiLikeGetter(Iterator<IGetter> getters, Object val) {
        return multiLikeGetter(true, getters, val);
    }

    /////////////////////////////////////////////////
    public <T, V1> ConditionWrapper<N> eq(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.EQ, val);
    }

    public <T, V1> ConditionWrapper<N> neq(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.NEQ, val);
    }

    public <T, V1> ConditionWrapper<N> gt(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.GT, val);
    }

    public <T, V1> ConditionWrapper<N> lt(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.LT, val);
    }

    public <T, V1> ConditionWrapper<N> gte(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.GTE, val);
    }

    public <T, V1> ConditionWrapper<N> lte(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.LTE, val);
    }

    public <T, V1> ConditionWrapper<N> in(ISetter<T, V1> setter, Iterator<?> val) {
        return cond(true, setter, Sql.IN, val);
    }

    public <T, V1> ConditionWrapper<N> like(ISetter<T, V1> setter, Object val) {
        return cond(true, setter, Sql.LIKE, val);
    }

    public <T, V1> ConditionWrapper<N> likes(ISetter<T, V1> setter, Iterator<?> val) {
        String name = DbFinder.dbFieldName(setter);
        return likes(true, name, val);
    }

    public ConditionWrapper<N> multiLikeSetter(Iterator<ISetter> setters, Object val) {
        return multiLikeSetter(true, setters, val);
    }

    @Override
    public BindSql prepare() {
        return BindSql.merge(conditions,"\n"+link.linker()+" ");
    }
}
