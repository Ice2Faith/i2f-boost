package i2f.generator.data;

import i2f.core.db.data.TableColumnMeta;
import i2f.extension.template.velocity.GeneratorTool;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 16:32
 * @desc
 */
@Data
public class ColumnContext {
    private String name;
    private String comment;

    private String pascalName;
    private String camelName;

    private String javaType;
    private String jdbcType;

    private String isAutoincrement="NO";

    private String isPrimaryKey="NO";

    public static ColumnContext parse(TableColumnMeta meta){
        ColumnContext ret=new ColumnContext();
        ret.name=meta.getName();
        ret.comment=meta.getRemark();

        ret.camelName= GeneratorTool.toCamel(GeneratorTool.lower(ret.name));
        ret.pascalName=GeneratorTool.toPascal(GeneratorTool.lower(ret.name));

        ret.javaType=meta.getJavaTypeString();

        if(ret.javaType!=null){
            if(ret.javaType.startsWith("java.lang.")){
                ret.javaType=ret.javaType.substring("java.lang.".length());
            }
            if(ret.javaType.equals(Timestamp.class.getName())){
                ret.javaType= Date.class.getSimpleName();
            }
        }
        ret.jdbcType=meta.getTypeName();

        ret.isAutoincrement=meta.getIsAutoincrement();
        ret.isPrimaryKey=meta.getIsPrimaryKey();
        return ret;
    }

    public static List<ColumnContext> parse(List<TableColumnMeta> metas){
        List<ColumnContext> ret=new ArrayList<>();
        for(TableColumnMeta item : metas){
            ColumnContext ctx=parse(item);
            ret.add(ctx);
        }
        return ret;
    }
}
