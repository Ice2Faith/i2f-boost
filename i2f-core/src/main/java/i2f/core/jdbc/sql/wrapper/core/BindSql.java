package i2f.core.jdbc.sql.wrapper.core;

import i2f.core.str.Appender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/23 17:34
 * @desc
 */
@Data
@NoArgsConstructor
public class BindSql {
    public String sql;
    public List<Object> params=new ArrayList<>();
    public BindSql(String sql){
        this.sql=sql;
    }
    public BindSql(String sql,List<Object> params){
        this.sql=sql;
        this.params=params;
    }
    public static BindSql merge(List<BindSql> list,Object separator){
        return merge(list,separator,null,null);
    }
    public static BindSql merge(List<BindSql> list,Object separator,Object open,Object close){
        List<String> parts=new ArrayList<>();
        List<Object> params=new ArrayList<>();
        for(BindSql item : list){
            parts.add(item.sql);
            params.addAll(item.params);
        }

        String sql= Appender.builder().addCollection(parts,separator,open,close).get();
        return new BindSql(sql,params);
    }

    public String show(){
        return Appender.builder()
                .addRepeat("-",20)
                .line()
                .add("sql:")
                .line()
                .add(sql)
                .line()
                .addRepeat("-",20)
                .line()
                .add("args:")
                .line()
                .addCollection(params, ",\n", "[\n", "\n]",(Object val)->{
                    return Appender.builder().adds(val,"(",(val==null?null:val.getClass().getSimpleName()),")").get();
                })
                .line()
                .addRepeat("-",20)
                .get();
    }
}
