package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Triple;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.core.DbFinder;
import i2f.core.lambda.funcs.IBuilder;
import i2f.core.lambda.funcs.IGetter;
import i2f.core.lambda.funcs.ISetter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:19
 * @desc
 */
public class KeyValueWrapper<N> implements INextWrapper<N> {
    protected N next;
    public List<Triple<String,String,Object>> kvs=new ArrayList<>();

    public KeyValueWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }
    //////////////////////////////////////
    public KeyValueWrapper<N> set(boolean cond,String key,String ope,Object val){
        if(!cond){
            return this;
        }
        kvs.add(new Triple<>(key,ope,val));
        return this;
    }
    public KeyValueWrapper<N> set(boolean cond,String key,Object val){
        return set(cond,key, Sql.EQ,val);
    }
    ////////////////////////////////////////
    public<T,R> KeyValueWrapper<N> set(boolean cond, IGetter<T,R> getter,String ope,Object val){
        String name = DbFinder.dbFieldName(getter);
        return set(cond,name,ope,val);
    }
    public<T,V1> KeyValueWrapper<N> set(boolean cond, ISetter<T,V1> setter, String ope, Object val){
        String name = DbFinder.dbFieldName(setter);
        return set(cond,name,ope,val);
    }
    public<T,R,V1> KeyValueWrapper<N> set(boolean cond, IBuilder<T,R,V1> builder, String ope, Object val){
        String name = DbFinder.dbFieldName(builder);
        return set(cond,name,ope,val);
    }
    public<T,R> KeyValueWrapper<N> set(boolean cond, IGetter<T,R> getter,Object val){
        String name = DbFinder.dbFieldName(getter);
        return set(cond,name,val);
    }
    public<T,V1> KeyValueWrapper<N> set(boolean cond, ISetter<T,V1> setter, Object val){
        String name = DbFinder.dbFieldName(setter);
        return set(cond,name,val);
    }
    public<T,R,V1> KeyValueWrapper<N> set(boolean cond, IBuilder<T,R,V1> builder, Object val){
        String name = DbFinder.dbFieldName(builder);
        return set(cond,name,val);
    }
    ///////////////////////////////////////////


    //////////////////////////////////////
    public KeyValueWrapper<N> set(String key,String ope,Object val){
        return set(true,key,ope,val);
    }
    public KeyValueWrapper<N> set(String key,Object val){
        return set(true,key, Sql.EQ,val);
    }
    ////////////////////////////////////////
    public<T,R> KeyValueWrapper<N> set( IGetter<T,R> getter,String ope,Object val){
        String name = DbFinder.dbFieldName(getter);
        return set(true,name,ope,val);
    }
    public<T,V1> KeyValueWrapper<N> set( ISetter<T,V1> setter, String ope, Object val){
        String name = DbFinder.dbFieldName(setter);
        return set(true,name,ope,val);
    }
    public<T,R,V1> KeyValueWrapper<N> set( IBuilder<T,R,V1> builder, String ope, Object val){
        String name = DbFinder.dbFieldName(builder);
        return set(true,name,ope,val);
    }
    public<T,R> KeyValueWrapper<N> set( IGetter<T,R> getter,Object val){
        String name = DbFinder.dbFieldName(getter);
        return set(true,name,val);
    }
    public<T,V1> KeyValueWrapper<N> set( ISetter<T,V1> setter, Object val){
        String name = DbFinder.dbFieldName(setter);
        return set(true,name,val);
    }
    public<T,R,V1> KeyValueWrapper<N> set( IBuilder<T,R,V1> builder, Object val){
        String name = DbFinder.dbFieldName(builder);
        return set(true,name,val);
    }


}
