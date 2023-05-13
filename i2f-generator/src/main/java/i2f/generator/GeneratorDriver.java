package i2f.generator;

import i2f.core.db.core.DbBeanResolver;
import i2f.core.db.core.DbResolver;
import i2f.core.db.data.TableMeta;
import i2f.core.reflect.Reflects;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.resource.ResourceUtil;
import i2f.core.xml.Xml2;
import i2f.extension.template.velocity.VelocityGenerator;
import i2f.generator.api.ApiLine;
import i2f.generator.api.ApiMethod;
import i2f.generator.api.ApiMethodResolver;
import i2f.generator.data.JavaCodeContext;
import i2f.generator.data.TableContext;
import i2f.generator.drawio.er.DrawioAdapter;
import i2f.generator.drawio.er.DrawioErElem;
import i2f.generator.er.ErContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author ltb
 * @date 2022/6/15 16:04
 * @desc
 */
public class GeneratorDriver {
    public static String generate(TableMeta table, String template, JavaCodeContext codeCtx) throws Exception {
        table.sortColumns();
        TableContext tableCtx = TableContext.parse(table);
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeCtx);
        map.put("table", tableCtx);
        String result = VelocityGenerator.render(template, map);
        return result;
    }

    public static void batch(TableMeta table, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        table.sortColumns();
        TableContext tableCtx = TableContext.parse(table);
        Map<String, Object> map = new HashMap<>();
        map.put("code", codeCtx);
        map.put("table", tableCtx);
        VelocityGenerator.batchRender(templatePath, map, outputPath, "UTF-8");
    }

    public static String generate(Connection conn, String tableName, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbResolver.getTableMeta(conn, tableName);
        return generate(table, template, codeCtx);
    }

    public static void batch(Connection conn, String tableName, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbResolver.getTableMeta(conn, tableName);
        batch(table, templatePath, outputPath, codeCtx);
    }

    public static String generate(Class beanClass, String template, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbBeanResolver.getTableMeta(beanClass);
        return generate(table, template, codeCtx);
    }

    public static void batch(Class beanClass, String templatePath, String outputPath, JavaCodeContext codeCtx) throws Exception {
        TableMeta table = DbBeanResolver.getTableMeta(beanClass);
        batch(table, templatePath, outputPath, codeCtx);
    }


    public static String er(List<TableMeta> tables, String template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        for (TableMeta item : tables) {
            item.sortColumns();
        }
        ErContext ctx = ErContext.parse(tables);
        map.put("er", ctx);
        return VelocityGenerator.render(template, map);
    }

    public static String er(String template, Class... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class item : beanClasses) {
            TableMeta table = DbBeanResolver.getTableMeta(item);
            list.add(table);
        }
        return er(list, template);
    }

    public static String er(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (String item : tableNames) {
            TableMeta table = DbResolver.getTableMeta(conn, item);
            list.add(table);
        }
        return er(list, template);
    }

    public static String drawioEr(List<TableMeta> tables, String template) throws IOException {
        Map<String, Object> map = new HashMap<>();
        List<DrawioErElem> elems = DrawioAdapter.parseEr(tables);
        map.put("elems", elems);
        return VelocityGenerator.render(template, map);
    }

    public static String drawioEr(List<TableMeta> tables) throws IOException {
        String tpl = ResourceUtil.getResourceAsString("tpl/drawio/er/er.xml.drawio.vm", "UTF-8");
        return drawioEr(tables, tpl);
    }

    public static String doc(List<TableMeta> tables, String template) throws Exception {
        Map<String, Object> map = new HashMap<>();
        for (TableMeta item : tables) {
            item.sortColumns();
        }
        map.put("tables", tables);
        return VelocityGenerator.render(template, map);
    }

    public static String doc(String template, Class... beanClasses) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (Class item : beanClasses) {
            TableMeta table = DbBeanResolver.getTableMeta(item);
            list.add(table);
        }
        return doc(list, template);
    }

    public static String doc(Connection conn, String template, String... tableNames) throws Exception {
        List<TableMeta> list = new ArrayList<>();
        for (String item : tableNames) {
            TableMeta table = DbResolver.getTableMeta(conn, item);
            list.add(table);
        }
        return doc(list, template);
    }


    public static String apis(List<ApiMethod> apis, String template) throws IOException {
        Map<String, Object> map = new HashMap<>();
        for (ApiMethod item : apis) {
            item.refresh(false, false);
            for (ApiLine line : item.getArgs()) {
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
            for (ApiLine line : item.getReturns()) {
                line.setTypeName(Xml2.toXmlString(line.getTypeName()));
                line.setComment(Xml2.toXmlString(line.getComment()));
            }
        }
        map.put("apis", apis);
        return VelocityGenerator.render(template, map);
    }

    public static String apiVo(Class voClass, String template) throws IOException {
        return apiVo(voClass, null, template);
    }

    public static String apiVo(Class voClass, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        Method[] methods = voClass.getMethods();
        Method[] declaredMethods = voClass.getDeclaredMethods();
        Set<Method> set = new HashSet<>();
        for (Method item : methods) {
            set.add(item);
        }
        for (Method item : declaredMethods) {
            set.add(item);
        }
        for (Method item : set) {
            apis.add(ApiMethodResolver.parseMethod(item, level));
        }
        return apis(apis, template);
    }

    public static String apiMethod(Method method, String template) throws IOException {
        return apiMethod(method, null, template);
    }

    public static String apiMethod(Method method, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        apis.add(ApiMethodResolver.parseMethod(method, level));
        return apis(apis, template);
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

    public static String apiMvc(Class clazz, String template) throws IOException {
        return apiMvc(clazz, null, template);
    }

    public static String apiMvc(Class clazz, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        List<ApiMethod> apis = getMvcApiMethods(clazz, level);
        return apis(apis, template);
    }

    public static List<ApiMethod> getMvcApiMethods(Class clazz) {
        return getMvcApiMethods(clazz, null);
    }

    public static List<ApiMethod> getMvcApiMethods(Class clazz, ApiMethodResolver.TraceLevel level) {
        List<ApiMethod> apis = new ArrayList<>();
        Set<Method> set = getMvcMethos(clazz);
        for (Method item : set) {
            apis.add(ApiMethodResolver.parseMethod(item, level));
        }
        return apis;
    }

    public static String apiMvcs(String template, Class clazz, Class... classes) throws IOException {
        return apiMvcs(null, template, clazz, classes);
    }

    public static String apiMvcs(ApiMethodResolver.TraceLevel level, String template, Class clazz, Class... classes) throws IOException {
        List<ApiMethod> apis = new ArrayList<>();
        apis.addAll(getMvcApiMethods(clazz, level));
        for (Class item : classes) {
            apis.addAll(getMvcApiMethods(item, level));
        }
        return apis(apis, template);
    }

    public static <T extends Annotation> Set<Class<?>> getSpringClassesWithAnnotations(ApplicationContext context, Class<T> annClazz) {
        Set<Class<?>> ret = new LinkedHashSet<>();
        Map<String, Object> beans = context.getBeansWithAnnotation(annClazz);
        for (Map.Entry<String, Object> entry : beans.entrySet()) {
            Object bean = entry.getValue();
            Class<?> clazz = bean.getClass();
            String clazzName = clazz.getName();
            int idx = clazzName.indexOf("$$Enhancer");
            if (idx >= 0) {
                clazzName = clazzName.substring(0, idx);
                clazz = Reflects.findClass(clazzName);
            }
            ret.add(clazz);
        }
        return ret;
    }

    public static Set<Class<?>> getSpringMvcControllers(ApplicationContext context) {
        Set<Class<?>> ret = new LinkedHashSet<>();
        ret.addAll(getSpringClassesWithAnnotations(context, RestController.class));
        ret.addAll(getSpringClassesWithAnnotations(context, Controller.class));
        return ret;
    }

    public static String apiMvcsSameComments(ApplicationContext context, Predicate<Class<?>> filter, String template) throws IOException {
        return apiMvcsSameComments(context, filter, null, template);
    }

    public static String apiMvcsSameComments(ApplicationContext context, Predicate<Class<?>> filter, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        return apiMvcsControllers(context, filter, (apis) -> {
            Map<String, String> commentMap = new HashMap<>();
            for (ApiMethod api : apis) {
                List<ApiLine> args = api.getArgs();
                for (ApiLine arg : args) {
                    String name = arg.getName();
                    String comment = arg.getComment();
                    if (!StringUtils.isEmpty(comment)) {
                        commentMap.put(name, comment);
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine line : returns) {
                    String name = line.getName();
                    String comment = line.getComment();
                    if (!StringUtils.isEmpty(comment)) {
                        commentMap.put(name, comment);
                    }
                }
            }

            for (ApiMethod api : apis) {
                List<ApiLine> args = api.getArgs();
                for (ApiLine arg : args) {
                    String name = arg.getName();
                    String comment = arg.getComment();
                    if (StringUtils.isEmpty(comment)) {
                        if (commentMap.containsKey(name)) {
                            arg.setComment(commentMap.get(name));
                        }
                    }
                }
                List<ApiLine> returns = api.getReturns();
                for (ApiLine line : returns) {
                    String name = line.getName();
                    String comment = line.getComment();
                    if (StringUtils.isEmpty(comment)) {
                        if (commentMap.containsKey(name)) {
                            line.setComment(commentMap.get(name));
                        }
                    }
                }
            }
        }, level, template);
    }

    public static String apiMvcsControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, String template) throws IOException {
        return apiMvcsControllers(context, filter, preProcessor, null, template);
    }

    public static String apiMvcsControllers(ApplicationContext context, Predicate<Class<?>> filter, Consumer<List<ApiMethod>> preProcessor, ApiMethodResolver.TraceLevel level, String template) throws IOException {
        Set<Class<?>> controllers = getSpringMvcControllers(context);
        List<ApiMethod> apis = new LinkedList<>();
        if (filter != null) {
            for (Class<?> controller : controllers) {
                if (filter.test(controller)) {
                    apis.addAll(getMvcApiMethods(controller, level));
                }
            }
        }

        if (preProcessor != null) {
            preProcessor.accept(apis);
        }

        return apis(apis, template);
    }


}
