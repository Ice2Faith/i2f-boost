package i2f.core.database.db.reverse;

import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.database.db.core.DbBeanResolver;
import i2f.core.database.db.core.DbResolver;
import i2f.core.database.db.data.TableColumnMeta;
import i2f.core.database.db.data.TableMeta;
import i2f.core.database.db.reverse.impl.BeanReverseProcessor;
import i2f.core.database.db.reverse.impl.TableBuildReverseProcessor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/23 21:23
 * @desc
 */
@Author("i2f")
public class DbReverseEngineer {
    public static Object ifnull(@Nullable Object val, @Nullable Object def){
        if(val==null){
            return def;
        }
        return val;
    }
    public static String checkReplaceAll(@Nullable String str,String src,String dst){
        if(str==null || "".equals(str)){
            return "";
        }
        return str.replaceAll(src,dst);
    }
    public static<T> T reverse(TableMeta meta,IReverseProcessor<T> processor){
        processor.onBegin(meta);

        List<TableColumnMeta> columns=meta.getColumns();
        processor.onBeginColumns(meta,columns);

        int renderIndex=0;
        boolean isRenderFirst=true;
        int size= columns.size();
        for(int i=0;i<size;i++){
            boolean ok=processor.onColumn(columns.get(i),meta,i, size,i==0,i==(size-1),isRenderFirst,renderIndex);
            if(ok){
                isRenderFirst=false;
                renderIndex++;
            }
        }

        processor.onEndColumns(meta,columns);
        processor.onEnd(meta);

        return processor.onResult();
    }
    public static List<String> makeCreateTableDDLs(boolean withDrop,boolean withCatalog,boolean withSchema,Class ... cls){
        List<String> ret=new ArrayList<>();
        for(Class item : cls){
            String sql=makeCreateTableDDL(item,withDrop,withCatalog,withSchema);
            ret.add(sql);
        }
        return ret;
    }
    public static String makeCreateTableDDL(Class clazz,boolean withDrop,boolean withCatalog,boolean withSchema){
        return reverse(DbBeanResolver.getTableMeta(clazz),new TableBuildReverseProcessor(withDrop,withCatalog,withSchema));
    }
    public static List<String> makeBeansByTables(Connection conn,@Nullable String pkg,boolean useLombok,String ... tableNames) throws SQLException {
        List<TableMeta> list=new ArrayList<>();
        for(String  item : tableNames){
            TableMeta meta=DbResolver.getTableMeta(conn,item);
            list.add(meta);
        }
        return makeBeansByTables(list,pkg,useLombok);
    }
    public static List<String> makeBeansByTables(List<TableMeta> metas,@Nullable String pkg,boolean useLombok) throws SQLException {
        List<String> ret=new ArrayList<>();
        for(TableMeta item : metas){
            String code=makeBeanCodeByTable(item,pkg,useLombok);
            ret.add(code);
        }
        return ret;
    }
    public static String makeBeanCodeByTable(Connection conn,String tableName,@Nullable String pkg,boolean useLombok) throws SQLException {
        TableMeta meta= DbResolver.getTableMeta(conn, tableName);
        return makeBeanCodeByTable(meta,pkg,useLombok);
    }
    public static String makeBeanCodeByTable(TableMeta meta,@Nullable String pkg,boolean useLombok) throws SQLException {
        return reverse(meta,new BeanReverseProcessor(pkg,useLombok));
    }

    public static String getShortTypeName(TableColumnMeta item){
        String type=item.getJavaTypeString();
        if(type.startsWith("java.lang.")
                || type.startsWith("java.util.")
                || type.startsWith("java.sql.")
                || type.startsWith("java.math.")){
            type=type.substring(type.lastIndexOf(".")+1);
        }
        return type;
    }
}
