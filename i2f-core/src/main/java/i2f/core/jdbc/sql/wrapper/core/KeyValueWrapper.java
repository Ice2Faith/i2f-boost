package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Triple;
import i2f.core.functional.common.IBuilder;
import i2f.core.functional.common.IGetter;
import i2f.core.functional.common.ISetter;
import i2f.core.jdbc.sql.consts.Sql;
import i2f.core.jdbc.sql.core.DbFinder;

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

    public KeyValueWrapper<N> set(boolean cond, String key, Object val) {
        return set(cond, key, Sql.EQ, val);
    }

    ////////////////////////////////////////
    public <R, T> KeyValueWrapper<N> set(boolean cond, IGetter<R, T> getter, String ope, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return set(cond, name, ope, val);
    }

    public <T, V1> KeyValueWrapper<N> set(boolean cond, ISetter<T, V1> setter, String ope, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return set(cond, name, ope, val);
    }

    public <R, T, V1> KeyValueWrapper<N> set(boolean cond, IBuilder<R, T, V1> builder, String ope, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return set(cond, name, ope, val);
    }

    public <R, T> KeyValueWrapper<N> set(boolean cond, IGetter<R, T> getter, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return set(cond, name, val);
    }

    public <T, V1> KeyValueWrapper<N> set(boolean cond, ISetter<T, V1> setter, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return set(cond, name, val);
    }

    public <R, T, V1> KeyValueWrapper<N> set(boolean cond, IBuilder<R, T, V1> builder, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return set(cond, name, val);
    }
    ///////////////////////////////////////////


    //////////////////////////////////////
    public KeyValueWrapper<N> set(String key,String ope,Object val){
        return set(true,key,ope,val);
    }

    public KeyValueWrapper<N> set(String key, Object val) {
        return set(true, key, Sql.EQ, val);
    }

    ////////////////////////////////////////
    public <R, T> KeyValueWrapper<N> set(IGetter<R, T> getter, String ope, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return set(true, name, ope, val);
    }

    public <T, V1> KeyValueWrapper<N> set(ISetter<T, V1> setter, String ope, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return set(true, name, ope, val);
    }

    public <R, T, V1> KeyValueWrapper<N> set(IBuilder<R, T, V1> builder, String ope, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return set(true, name, ope, val);
    }

    public <R, T> KeyValueWrapper<N> set(IGetter<R, T> getter, Object val) {
        String name = DbFinder.dbFieldName(getter);
        return set(true, name, val);
    }

    public <T, V1> KeyValueWrapper<N> set(ISetter<T, V1> setter, Object val) {
        String name = DbFinder.dbFieldName(setter);
        return set(true, name, val);
    }

    public <R, T, V1> KeyValueWrapper<N> set(IBuilder<R, T, V1> builder, Object val) {
        String name = DbFinder.dbFieldName(builder);
        return set(true, name, val);
    }


}
