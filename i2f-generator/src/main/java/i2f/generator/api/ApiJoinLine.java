package i2f.generator.api;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Remark;
import i2f.core.db.annotations.DbComment;
import i2f.core.db.data.TableColumnMeta;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;
import i2f.core.str.Appender;
import i2f.extension.template.velocity.GeneratorTool;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/16 13:54
 * @desc
 */
@Data
public class ApiJoinLine {
    private String parent;
    private String name;
    private String restrict;
    private String type;
    private String width;
    private String comment;
    private String remark;
    public static List<ApiJoinLine> parse(TableMeta meta,String parent){
        List<ApiJoinLine> ret=new ArrayList<>();
        for(TableColumnMeta item : meta.getColumns()){
            ApiJoinLine join=new ApiJoinLine();
            join.parent=parent;
            join.name= GeneratorTool.toCamel(GeneratorTool.lower(item.getName()));
            join.restrict=item.getIsNullable();
            join.type=item.getJavaType().getSimpleName();
            join.width=item.getPrecision()+"";
            join.comment=item.getRemark();
            join.remark=null;
            ret.add(join);
        }

        return ret;
    }

    public static List<ApiJoinLine> parseMethod(Method method){
        List<ApiJoinLine> ret=new ArrayList<>();
        Parameter[] parameters=method.getParameters();
        for(Parameter item : parameters){
            String name=item.getName();
            Name ann=ReflectResolver.findAnnotation(item,Name.class,false);
            if(ann!=null){
                name=ann.value();
            }
            List<ApiJoinLine> lines=parseVo(item.getType(),name);
            ret.addAll(lines);
        }

        return ret;
    }

    public static List<ApiJoinLine> parseVo(Class clazz,String parent){
        List<ApiJoinLine> ret=new ArrayList<>();
        if(clazz.getName().startsWith("java.lang.") || clazz.getName().startsWith("java.util.")){
            ApiJoinLine join=new ApiJoinLine();
            join.parent=null;
            join.name= parent;
            join.restrict=null;
            join.type= clazz.getSimpleName();
            join.width=null;
            join.comment=null;
            join.remark=null;
            ret.add(join);
            return ret;
        }
        List<PropertyAccessor> fields = ReflectResolver.getLogicalReadWriteFields(clazz);
        for(PropertyAccessor item : fields){
            Field field = item.getField();
            Class<?> type = field.getType();
            if(type.getName().startsWith("java.lang.") || type.getName().startsWith("java.util.")){
                ApiJoinLine join=new ApiJoinLine();
                join.parent=parent;
                join.name= item.getName();
                join.restrict=null;
                join.type=type.getSimpleName();
                join.width=null;
                join.comment=null;
                DbComment dbann=ReflectResolver.findAnnotation(field,DbComment.class,false);
                if(dbann!=null){
                    join.comment=dbann.value();
                }
                Remark ann=ReflectResolver.findAnnotation(field,Remark.class,false);
                if(ann!=null){
                    String comment= Appender.sepStr(" \n",ann.value());
                    join.comment=comment;
                }
                join.remark=null;
                ret.add(join);
            }else{
                List<ApiJoinLine> nexts=parseVo(type, field.getName());
                ret.addAll(nexts);
            }
        }

        return ret;
    }

}
