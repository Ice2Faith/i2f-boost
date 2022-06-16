package i2f.generator;

import i2f.core.db.core.DbBeanResolver;
import i2f.core.db.core.DbResolver;
import i2f.core.db.data.TableMeta;
import i2f.extension.template.velocity.VelocityGenerator;
import i2f.generator.data.JavaCodeContext;
import i2f.generator.data.TableContext;
import i2f.generator.er.ErContext;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ltb
 * @date 2022/6/15 16:04
 * @desc
 */
public class GeneratorDriver {
    public static String generate(TableMeta table, String template, JavaCodeContext codeCtx) throws Exception {
        table.sortColumns();
        TableContext tableCtx=TableContext.parse(table);
        Map<String,Object> map=new HashMap<>();
        map.put("code",codeCtx);
        map.put("table",tableCtx);
        String result = VelocityGenerator.render(template, map);
        return result;
    }
    public static void batch(TableMeta table,String templatePath,String outputPath,JavaCodeContext codeCtx) throws Exception {
        table.sortColumns();
        TableContext tableCtx=TableContext.parse(table);
        Map<String,Object> map=new HashMap<>();
        map.put("code",codeCtx);
        map.put("table",tableCtx);
        VelocityGenerator.batchRender(templatePath,map, outputPath,"UTF-8");
    }
    public static String generate(Connection conn, String tableName, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbResolver.getTableMeta(conn, tableName);
        return generate(table,template,codeCtx);
    }
    public static void batch(Connection conn,String tableName,String templatePath,String outputPath,JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbResolver.getTableMeta(conn, tableName);
        batch(table,templatePath,outputPath,codeCtx);
    }
    public static String generate(Class beanClass, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbBeanResolver.getTableMeta(beanClass);
        return generate(table,template,codeCtx);
    }
    public static void batch(Class beanClass,String templatePath,String outputPath,JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbBeanResolver.getTableMeta(beanClass);
        batch(table,templatePath,outputPath,codeCtx);
    }




    public static String er(List<TableMeta> tables, String template) throws Exception {
        Map<String,Object> map=new HashMap<>();
        for(TableMeta item : tables){
            item.sortColumns();
        }
        ErContext ctx=ErContext.parse(tables);
        map.put("er",ctx);
        return VelocityGenerator.render(template,map);
    }
    public static String er(String template,Class ... beanClasses) throws Exception {
        List<TableMeta> list=new ArrayList<>();
        for(Class item : beanClasses){
            TableMeta table = DbBeanResolver.getTableMeta(item);
            list.add(table);
        }
        return er(list,template);
    }

    public static String er(Connection conn,String template,String ... tableNames) throws Exception {
        List<TableMeta> list=new ArrayList<>();
        for(String item : tableNames){
            TableMeta table = DbResolver.getTableMeta(conn, item);
            list.add(table);
        }
        return er(list,template);
    }



    public static String doc(List<TableMeta> tables, String template) throws Exception {
        Map<String,Object> map=new HashMap<>();
        for(TableMeta item : tables){
            item.sortColumns();
        }
        map.put("tables",tables);
        return VelocityGenerator.render(template,map);
    }
    public static String doc(String template,Class ... beanClasses) throws Exception {
        List<TableMeta> list=new ArrayList<>();
        for(Class item : beanClasses){
            TableMeta table = DbBeanResolver.getTableMeta(item);
            list.add(table);
        }
        return doc(list,template);
    }

    public static String doc(Connection conn,String template,String ... tableNames) throws Exception {
        List<TableMeta> list=new ArrayList<>();
        for(String item : tableNames){
            TableMeta table = DbResolver.getTableMeta(conn, item);
            list.add(table);
        }
        return doc(list,template);
    }


}
