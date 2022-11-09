package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.data.Triple;
import i2f.core.jdbc.sql.core.DbFinder;
import i2f.core.lambda.functional.preset.IBeanBuilder;
import i2f.core.lambda.functional.preset.IBeanGetter;
import i2f.core.lambda.functional.preset.IBeanSetter;
import i2f.core.str.Appender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:19
 * @desc
 */
public class DdlColumnWrapper<N> implements INextWrapper<N> {
    protected N next;
    public List<Triple<String,String,Object>> columns=new ArrayList<>();

    public DdlColumnWrapper(N next){
        this.next=next;
    }

    @Override
    public N next(){
        return next;
    }

    //////////////////////////////////////
    public DdlColumnWrapper<N> col(String name, String type, String... restricts) {
        String restrict = Appender.builder().addsFull(" ", null, null, restricts).get();
        columns.add(new Triple<>(name, type, restrict));
        return this;
    }

    public <T, R> DdlColumnWrapper<N> col(IBeanGetter<T, R> getter, String type, String... restricts) {
        String name = DbFinder.dbFieldName(getter);
        return col(name, type, restricts);
    }

    public <T, V1> DdlColumnWrapper<N> col(IBeanSetter<T, V1> setter, String type, String... restricts) {
        String name = DbFinder.dbFieldName(setter);
        return col(name, type, restricts);
    }

    public <R, T, V1> DdlColumnWrapper<N> col(IBeanBuilder<R, T, V1> builder, String type, String... restricts) {
        String name = DbFinder.dbFieldName(builder);
        return col(name, type, restricts);
    }

    public String columnDefines(){

        return Appender.builder().addCollection(columns, ",\n\t",null,null,(Object val)->{
            Triple<String,String,Object> tval=(Triple<String,String,Object>)val;
            return Appender.builder().addsSep(" ",tval.fst,tval.sec,tval.trd).get();
        }).get();
    }

}
