package i2f.generator;

import i2f.core.db.core.DbBeanResolver;
import i2f.core.db.core.DbResolver;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.Reflects;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.xml.Xml2;
import i2f.extension.template.velocity.VelocityGenerator;
import i2f.generator.api.ApiLine;
import i2f.generator.api.ApiMethod;
import i2f.generator.api.ApiMethodResolver;
import i2f.generator.data.JavaCodeContext;
import i2f.generator.data.TableContext;
import i2f.generator.er.ErContext;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;

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


    public static String apis(List<ApiMethod> apis, String template) throws IOException {
        Map<String,Object> map=new HashMap<>();
        for(ApiMethod item : apis){
            item.refresh(false,false);
            for(ApiLine line : item.getArgs()){
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
            for(ApiLine line : item.getReturns()){
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
        }
        map.put("apis",apis);
        return VelocityGenerator.render(template,map);
    }

    public static String apiVo(Class voClass,String template) throws IOException {
        List<ApiMethod> apis=new ArrayList<>();
        Method[] methods = voClass.getMethods();
        Method[] declaredMethods = voClass.getDeclaredMethods();
        Set<Method> set=new HashSet<>();
        for(Method item : methods){
            set.add(item);
        }
        for(Method item : declaredMethods){
            set.add(item);
        }
        for(Method item : set){
            apis.add(ApiMethodResolver.parseMethod(item));
        }
        return apis(apis,template);
    }

    public static String apiMethod(Method method, String template) throws IOException {
        List<ApiMethod> apis=new ArrayList<>();
        apis.add(ApiMethodResolver.parseMethod(method));
        return apis(apis,template);
    }

    public static Set<Method> getMvcMethos(Class clazz) {
        String clazzName = clazz.getName();
        int idx = clazzName.indexOf("$$EnhancerBySpring");
        if (idx >= 0) {
            clazzName = clazzName.substring(0, idx);
            clazz = Reflects.findClass(clazzName);
        }
        Set<Method> set = ReflectResolver.getMethodsWithAnnotations(clazz, false,
                RequestMapping.class,
                GetMapping.class, PostMapping.class, PutMapping.class, DeleteMapping.class,
                PatchMapping.class);
        return set;
    }

    public static String apiMvc(Class clazz,String template) throws IOException {
        List<ApiMethod> apis=new ArrayList<>();
        Set<Method> set = getMvcMethos(clazz);
        for(Method item : set){
            apis.add(ApiMethodResolver.parseMethod(item));
        }
        return apis(apis,template);
    }

    public static List<ApiMethod> getMvcApiMethods(Class clazz){
        List<ApiMethod> apis=new ArrayList<>();
        Set<Method> set = getMvcMethos(clazz);
        for(Method item : set){
            apis.add(ApiMethodResolver.parseMethod(item));
        }
        return apis;
    }

    public static String apiMvcs(String template,Class clazz,Class ... classes) throws IOException {
        List<ApiMethod> apis=new ArrayList<>();
        apis.addAll(getMvcApiMethods(clazz));
        for(Class item : classes){
            apis.addAll(getMvcApiMethods(item));
        }
        return apis(apis,template);
    }


}
