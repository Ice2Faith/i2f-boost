package i2f.core.database.db.reverse.impl;

import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.database.db.annotations.DbName;
import i2f.core.database.db.data.TableColumnMeta;
import i2f.core.database.db.data.TableMeta;
import i2f.core.database.db.reverse.DbReverseEngineer;
import i2f.core.database.db.reverse.IReverseProcessor;
import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.type.str.Strings;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/23 23:36
 * @desc
 */
@Author("i2f")
public class BeanReverseProcessor implements IReverseProcessor<String> {
    private StringBuilder builder;
    private String className;
    private String pkg="com";
    private boolean useLombok=true;
    public BeanReverseProcessor(@Nullable String pkg, boolean useLombok){
        this.pkg=pkg;
        this.useLombok=useLombok;
    }

    @Override
    public void onBegin(TableMeta meta) {
        builder = new StringBuilder();
        className = Strings.toPascal(meta.getTable());
    }

    @Override
    public void onBeginColumns(TableMeta meta, List<TableColumnMeta> columns) {
        Class clazz= DbName.class;
        String annotationPackage=ReflectResolver.getPackage(clazz);
        builder.append("package "+ DbReverseEngineer.ifnull(pkg,"com")+";\n");
        builder.append("\n");
        builder.append("import "+annotationPackage+".*;\n");
        builder.append("\n");
        builder.append("import java.io.Serializable;\n");
        builder.append("import java.util.*;\n");
        builder.append("import java.math.*;\n");
        builder.append("import java.sql.*;\n");
        if(useLombok){
            builder.append("import lombok.Data;\n");
            builder.append("import lombok.NoArgsConstructor;\n");
        }
        builder.append("\n");
        builder.append("@DbComment(\""+DbReverseEngineer.checkReplaceAll(meta.getRemark(),"\"","\\\"")+"\")\n");
        builder.append("@DbCatalog(\""+DbReverseEngineer.ifnull(meta.getCatalog(),"")+"\")\n");
        builder.append("@DbSchema(\""+DbReverseEngineer.ifnull(meta.getSchema(),"")+"\")\n");
        builder.append("@DbName(\""+meta.getTable()+"\")\n");
        builder.append("@DbType(\""+meta.getType()+"\")\n");
        if(useLombok){
            builder.append("@Data\n");
            builder.append("@NoArgsConstructor\n");
        }
        builder.append("public class "+className+" implements Serializable {\n");
        builder.append("\tprivate static final long serialVersionUID = -1L;\n");
        builder.append("\n");
    }

    @Override
    public boolean onColumn(TableColumnMeta column, TableMeta meta, int index, int size, boolean isFirst, boolean isEnd, boolean isRenderFirst, int renderIndex) {
        builder.append("\t@DbComment(\""+DbReverseEngineer.checkReplaceAll(column.getRemark(),"\"","\\\"")+"\")\n");
        builder.append("\t@DbName(\""+column.getName()+"\")\n");
        String scale="";
        Object iscale=DbReverseEngineer.ifnull(column.getScale(),0);
        if(!iscale.equals(0)){
            scale=",scale="+iscale;
        }
        builder.append("\t@DbType(value=\""+column.getTypeName()+"\",precision="+DbReverseEngineer.ifnull(column.getPrecision(),0)+scale+")\n");
        if("NO".equals(column.getIsNullable())){
            builder.append("\t@DbNullable(false)\n");
        }
        if("YES".equals(column.getIsAutoincrement())){
            builder.append("\t@DbAutoIncrement\n");
        }
        if("YES".equals(column.getIsPrimaryKey())){
            builder.append("\t@DbPrimaryKey(key=\""+column.getPrimaryKeyName()+"\")\n");
        }
        if("YES".equals(column.getIsUnique())){
            builder.append("\t@DbUnique(key=\""+column.getUniqueKeyName()+"\")\n");
        }
        if(column.getColumnDef()!=null && !"".equals(column.getColumnDef())){
            builder.append("\t@DbDefault(\""+DbReverseEngineer.checkReplaceAll(column.getColumnDef(),"\"","\\\"")+"\")\n");
        }
        if("YES".equals(column.getIsIndex())){
            String indexType="";
            if(column.getIndexType()!=null && !"".equals(column.getIndexType())){
                indexType=",type = \""+column.getIndexType()+"\"";
            }
            String indexUsing="";
            if(column.getIndexUsing()!=null && !"".equals(column.getIndexUsing())){
                indexType=",using = \""+column.getIndexUsing()+"\"";
            }
            builder.append("\t@DbIndex(key=\""+column.getIndexKeyName()+"\""+indexType+")\n");
        }
        builder.append("\tprivate " + DbReverseEngineer.getShortTypeName(column) + " " + Strings.toCamel(column.getName()) + ";\n");
        builder.append("\n");
        return true;
    }

    @Override
    public void onEndColumns(TableMeta meta, List<TableColumnMeta> columns) {
        if(!useLombok){
            for(TableColumnMeta item : columns) {
                String type = DbReverseEngineer.getShortTypeName(item);
                String variableName = Strings.toCamel(item.getName());
                String variablePascalName = Strings.toPascal(item.getName());
                builder.append("\tpublic " + type + " get" + variablePascalName + "() {\n");
                builder.append("\t\treturn " + variableName + ";\n");
                builder.append("\t}\n");
                builder.append("\n");
                builder.append("\tpublic " + className + " set" + variablePascalName + "(" + type + " " + variableName + ") {\n");
                builder.append("\t\tthis." + variableName + " = " + variableName + ";\n");
                builder.append("\t\treturn this;\n");
                builder.append("\t}\n");
                builder.append("\n");
            }

        }
        builder.append("}\n");
        builder.append("\n");
    }

    @Override
    public void onEnd(TableMeta meta) {

    }

    @Override
    public String onResult() {
        return builder.toString();
    }
}
