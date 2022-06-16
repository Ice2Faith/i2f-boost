package i2f.core.reflect.core;


import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.check.CheckUtil;
import i2f.core.collection.CollectionUtil;
import i2f.core.reflect.convert.ConvertResolver;
import i2f.core.reflect.exception.InstanceException;
import i2f.core.reflect.exception.MethodAccessFoundException;
import i2f.core.reflect.exception.MethodNotFoundException;
import i2f.core.reflect.impl.FieldValueAccessor;
import i2f.core.reflect.impl.MethodValueAccessor;
import i2f.core.reflect.interfaces.PropertyAccessor;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/14 11:03
 * @desc 反射核心处理类
 * 设计原则
 * 优先使用缓存查找方式，一定程度上降低反射带来的效率问题
 * 缓存中的数据不对外进行修改，防止反射异常，使用容器拷贝方式解决
 */
@Author("i2f")
public class ReflectResolver {
    protected static volatile ConcurrentHashMap<Class, Set<Constructor>> cacheConstructors = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Set<Field>> cacheFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheMethods = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheGetters = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Set<Method>> cacheSetters = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Map<String, Set<Method>>> fastGetter = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Map<String, Set<Method>>> fastSetter = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, Map<String, Field>> fastField = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, List<PropertyAccessor>> logicalReadableFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class, List<PropertyAccessor>> logicalWritableFields = new ConcurrentHashMap<>();
    protected static volatile ConcurrentHashMap<Class,List<PropertyAccessor>> logicalReadWriteFields=new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Class, Set<Field>> cacheForceFields = new ConcurrentHashMap<>();

    protected static volatile ConcurrentHashMap<Method,String[]> cacheParameterNames=new ConcurrentHashMap<>();

    public static String getClassName(Class clazz){
        return clazz.getSimpleName();
    }
    public static String getFullClassName(Class clazz){
        return clazz.getName();
    }
    public static String getPackage(Class clazz){
        String name=getFullClassName(clazz);
        int idx=name.lastIndexOf(".");
        if(idx>=0){
            return name.substring(0,idx);
        }
        return "";
    }
    public static Class getClazz(String className) {
        Class clazz = null;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {

        }
        return clazz;
    }

    public static <T> Class<T> getClazz(T obj) {
        if (CheckUtil.isNull(obj)) {
            return null;
        }
        Class<T> clazz = (Class<T>) obj.getClass();
        return clazz;
    }
    /**
     * 获取指定类的逻辑上可读的所有属性
     * 认为本类字段以及所有getter包含的都是逻辑上的可读字段
     *
     * @param clazz
     * @return
     */
    public static List<PropertyAccessor> getLogicalReadableFields(Class clazz) {
        if (logicalReadableFields.containsKey(clazz)) {
            return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),logicalReadableFields.get(clazz));
        }
        List<PropertyAccessor> ret = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        Set<Method> methods = getAllGetters(clazz);
        for (Method item : methods) {
            String name = item.getName();
            if (name.startsWith("get")) {
                name = name.substring("get".length());
            } else if (name.startsWith("is")) {
                name = name.substring("is".length());
            }
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            MethodValueAccessor accessor = new MethodValueAccessor(item, null, name, item.getReturnType(),findField(clazz,name));
            ret.add(accessor);
        }

        Set<Field> fieldSet = getAllFields(clazz);
        for (Field item : fieldSet) {
            String name = item.getName();
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            FieldValueAccessor accessor = new FieldValueAccessor(item);
            ret.add(accessor);
        }

        logicalReadableFields.put(clazz, ret);
        return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),ret);
    }

    /**
     * 获取指定类的逻辑上可写的所有属性
     * 认为本类字段以及所有setter包含的都是逻辑上可写的字段
     *
     * @param clazz
     * @return
     */
    public static List<PropertyAccessor> getLogicalWritableFields(Class clazz) {
        if (logicalWritableFields.containsKey(clazz)) {
            return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),logicalWritableFields.get(clazz));
        }
        List<PropertyAccessor> ret = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        Set<Method> methods = getAllSetters(clazz);
        for (Method item : methods) {
            String name = item.getName();
            if (name.startsWith("set")) {
                name = name.substring("set".length());
            }
            name = name.substring(0, 1).toLowerCase() + name.substring(1);
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            MethodValueAccessor accessor = new MethodValueAccessor(null, item, name, item.getParameterTypes()[0],findField(clazz,name));
            ret.add(accessor);
        }

        Set<Field> fieldSet = getAllFields(clazz);
        for (Field item : fieldSet) {
            String name = item.getName();
            if (fields.contains(name)) {
                continue;
            }
            fields.add(name);
            FieldValueAccessor accessor = new FieldValueAccessor(item);
            ret.add(accessor);
        }

        logicalWritableFields.put(clazz, ret);
        return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),ret);
    }

    public static List<PropertyAccessor> getLogicalReadWriteFields(Class clazz){
        if(logicalReadWriteFields.containsKey(clazz)){
            return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),logicalReadWriteFields.get(clazz));
        }
        List<PropertyAccessor> ret=new ArrayList<>();
        Set<String> set=new HashSet<>();
        List<PropertyAccessor> writers=getLogicalWritableFields(clazz);
        List<PropertyAccessor> readers=getLogicalReadableFields(clazz);
        for(PropertyAccessor witem : writers){
            String wname=witem.getName();
            if(witem instanceof FieldValueAccessor){
                if(!set.contains(wname)){
                    set.add(wname);
                    ret.add(witem);
                    continue;
                }
            }

            for(PropertyAccessor ritem : readers){
                String rname= ritem.getName();
                if(ritem instanceof FieldValueAccessor){
                    continue;
                }
                if(rname.equals(wname)){
                    if(set.contains(rname)){
                        continue;
                    }
                    MethodValueAccessor racc=(MethodValueAccessor)ritem;
                    MethodValueAccessor wacc=(MethodValueAccessor)witem;
                    PropertyAccessor accessor=new MethodValueAccessor(racc.getGetter(),wacc.getSetter(),rname,ritem.getType(),findField(clazz,rname));
                    ret.add(accessor);
                    set.add(rname);
                }
            }
        }

        logicalReadWriteFields.put(clazz,ret);
        return CollectionUtil.toCollection(new ArrayList<PropertyAccessor>(),ret);
    }

    public static List<PropertyAccessor> getLogicalReadableFieldsWithAnnotations(Class clazz,boolean ckAnnotatedAnn,Class<? extends Annotation> ... annTypes){
        List<PropertyAccessor> list=getLogicalReadWriteFields(clazz);
        return getFieldsWithAnnotationsProxy(list,ckAnnotatedAnn,annTypes);
    }
    public static List<PropertyAccessor> getLogicalWritableFieldsWithAnnotations(Class clazz,boolean ckAnnotatedAnn,Class<? extends Annotation> ... annTypes){
        List<PropertyAccessor> list=getLogicalWritableFields(clazz);
        return getFieldsWithAnnotationsProxy(list,ckAnnotatedAnn,annTypes);
    }
    public static List<PropertyAccessor> getLogicalReadWriteFieldsWithAnnotations(Class clazz,boolean ckAnnotatedAnn,Class<? extends Annotation> ... annTypes){
        List<PropertyAccessor> list=getLogicalReadWriteFields(clazz);
        return getFieldsWithAnnotationsProxy(list,ckAnnotatedAnn,annTypes);
    }
    public static List<PropertyAccessor> getFieldsWithAnnotationsProxy(List<PropertyAccessor> list,boolean ckAnnotatedAnn,Class<? extends Annotation> ... annTypes){
        List<PropertyAccessor> ret=new ArrayList<>();
        if(annTypes==null || annTypes.length==0){
            return ret;
        }
        for(PropertyAccessor item : list){
            Field field=item.getField();
            for(Class ann : annTypes){
                Annotation tag=ReflectResolver.findAnnotation(field, ann,ckAnnotatedAnn);
                if(tag!=null){
                    ret.add(item);
                    break;
                }
            }
        }
        return ret;
    }

    protected static String[] getParameterNames(Method method){
        if(cacheParameterNames.containsKey(method)){
            return cacheParameterNames.get(method);
        }
        Parameter[] params = method.getParameters();
        String[] names=new String[params.length];
        for(int i=0;i<params.length;i+=1){
            names[i]=params[i].getName();
            Name ann=findAnnotation(params[i],Name.class,false);
            if(ann!=null && !"".equals(ann.value())){
                names[i]=ann.value();
            }
        }

        cacheParameterNames.put(method,names);
        return names;
    }

    protected static boolean containsFastGetterProxy(Class clazz, String fieldName) {
        if (fastGetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastGetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastGetterProxy(Class clazz, String fieldName) {
        if (fastGetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastGetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return CollectionUtil.toCollection(new HashSet<Method>(),map.get(fieldName));
            }
        }
        return new HashSet<>();
    }

    protected static void setFastGetterProxy(Class clazz, String fieldName, Set<Method> methods) {
        if (!fastGetter.containsKey(clazz)) {
            fastGetter.put(clazz, new HashMap<>());
        }
        Map<String, Set<Method>> map = fastGetter.get(clazz);
        map.put(fieldName, methods);
    }

    protected static boolean containsFastSetterProxy(Class clazz, String fieldName) {
        if (fastSetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastSetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Set<Method> getFastSetterProxy(Class clazz, String fieldName) {
        if (fastSetter.containsKey(clazz)) {
            Map<String, Set<Method>> map = fastSetter.get(clazz);
            if (map.containsKey(fieldName)) {
                return CollectionUtil.toCollection(new HashSet<Method>(),map.get(fieldName));
            }
        }
        return new HashSet<>();
    }

    protected static void setFastSetterProxy(Class clazz, String fieldName, Set<Method> methods) {
        if (!fastSetter.containsKey(clazz)) {
            fastSetter.put(clazz, new HashMap<>());
        }
        Map<String, Set<Method>> map = fastSetter.get(clazz);
        map.put(fieldName, methods);
    }

    protected static boolean containsFastFieldProxy(Class clazz, String fieldName) {
        if (fastField.containsKey(clazz)) {
            Map<String, Field> map = fastField.get(clazz);
            if (map.containsKey(fieldName)) {
                return true;
            }
        }
        return false;
    }

    protected static Field getFastFieldProxy(Class clazz, String fieldName) {
        if (fastField.containsKey(clazz)) {
            Map<String, Field> map = fastField.get(clazz);
            if (map.containsKey(fieldName)) {
                return map.get(fieldName);
            }
        }
        return null;
    }

    protected static void setFastFieldProxy(Class clazz, String fieldName, Field field) {
        if (!fastField.containsKey(clazz)) {
            fastField.put(clazz, new HashMap<>());
        }
        Map<String, Field> map = fastField.get(clazz);
        map.put(fieldName, field);
    }

    public static Set<Constructor> getAllConstructors(Class clazz){
        if (cacheConstructors.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Constructor>(),cacheConstructors.get(clazz));
        }
        Set<Constructor> ret = new HashSet<>();
        for (Constructor item : clazz.getConstructors()) {
            ret.add(item);
        }
        for (Constructor item : clazz.getDeclaredConstructors()) {
            ret.add(item);
        }
        cacheConstructors.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Constructor>(),ret);
    }

    /**
     * 获取指定类的所有方法
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllMethods(Class clazz) {
        if (cacheMethods.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Method>(),cacheMethods.get(clazz));
        }
        Set<Method> ret = new HashSet<>();
        for (Method item : clazz.getMethods()) {
            ret.add(item);
        }
        for (Method item : clazz.getDeclaredMethods()) {
            ret.add(item);
        }
        cacheMethods.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Method>(),ret);
    }

    /**
     * 获取指定类的所有getter
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllGetters(Class clazz) {
        if (cacheGetters.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Method>(),cacheGetters.get(clazz));
        }
        Set<Method> ret = new HashSet<>();
        Set<Method> methods = getAllMethods(clazz);
        for (Method item : methods) {
            if (methodIsGetter(item)) {
                ret.add(item);
            }
        }
        cacheGetters.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Method>(),ret);
    }

    /**
     * 获取指定类的所有setter
     *
     * @param clazz
     * @return
     */
    public static Set<Method> getAllSetters(Class clazz) {
        if (cacheSetters.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Method>(),cacheSetters.get(clazz));
        }
        Set<Method> ret = new HashSet<>();
        Set<Method> methods = getAllMethods(clazz);
        for (Method item : methods) {
            if (methodIsSetter(item)) {
                ret.add(item);
            }
        }
        cacheSetters.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Method>(),ret);
    }

    /**
     * 获取指定类的所有字段，仅本类
     *
     * @param clazz
     * @return
     */
    public static Set<Field> getAllFields(Class clazz) {
        if (cacheFields.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Field>(),cacheFields.get(clazz));
        }
        Set<Field> ret = new HashSet<>();
        for (Field item : clazz.getFields()) {
            ret.add(item);
        }
        for (Field item : clazz.getDeclaredFields()) {
            ret.add(item);
        }
        cacheFields.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Field>(),ret);
    }

    /**
     * 获取指定类的指定字段的setter
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Set<Method> findGetters(Class clazz, String fieldName) {
        if (containsFastGetterProxy(clazz, fieldName)) {
            return getFastGetterProxy(clazz, fieldName);
        }
        Set<Method> ret = new HashSet<>();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method item : getAllGetters(clazz)) {
            String name = item.getName();
            if (name.equals("get" + methodName) || name.equals("is" + methodName)) {
                ret.add(item);
            }
        }
        setFastGetterProxy(clazz, fieldName, ret);
        return CollectionUtil.toCollection(new HashSet<Method>(),ret);
    }

    /**
     * 获取指定类的指定字段的getter
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Set<Method> findSetters(Class clazz, String fieldName) {
        if (containsFastSetterProxy(clazz, fieldName)) {
            return getFastSetterProxy(clazz, fieldName);
        }
        Set<Method> ret = new HashSet<>();
        String methodName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        for (Method item : getAllSetters(clazz)) {
            String name = item.getName();
            if (name.equals("set" + methodName)) {
                ret.add(item);
            }
        }
        setFastSetterProxy(clazz, fieldName, ret);
        return CollectionUtil.toCollection(new HashSet<Method>(),ret);
    }

    /**
     * 获取指定类中的指定名称的字段
     * 本类未找到，则尝试从父类中查找
     *
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field findField(Class clazz, String fieldName) {
        if (containsFastFieldProxy(clazz, fieldName)) {
            return getFastFieldProxy(clazz, fieldName);
        }
        Field field = null;
        Set<Field> fields = getAllFields(clazz);
        for (Field item : fields) {
            String name = item.getName();
            if (name.equals(fieldName)) {
                field = item;
                break;
            }
        }
        if (field == null) {
            Set<Field> force = forceAllFields(clazz);
            for (Field item : force) {
                String name = item.getName();
                if (name.equals(fieldName)) {
                    field = item;
                    break;
                }
            }
        }
        setFastFieldProxy(clazz, fieldName, field);
        return field;
    }

    /**
     * 判断方法是否是一个标准的getter
     *
     * @param method
     * @return
     */
    public static boolean methodIsGetter(Method method) {
        if (method.getParameterCount() != 0) {
            return false;
        }
        if (method.getReturnType().equals(Void.class)) {
            return false;
        }
        if (!method.getName().startsWith("get") && !method.getName().startsWith("is")) {
            return false;
        }
        return true;
    }

    /**
     * 判断方法是否是一个标准的setter
     *
     * @param method
     * @return
     */
    public static boolean methodIsSetter(Method method) {
        if (method.getParameterCount() != 1) {
            return false;
        }
        if (!method.getName().startsWith("set")) {
            return false;
        }
        return true;
    }

    /**
     * 强制获取指定类的所有字段，包含父类中的私有字段
     *
     * @param clazz
     * @return
     */
    public static Set<Field> forceAllFields(Class clazz) {
        if (cacheForceFields.containsKey(clazz)) {
            return CollectionUtil.toCollection(new HashSet<Field>(),cacheForceFields.get(clazz));
        }
        Set<Field> ret = new HashSet<>();
        if (null == clazz) {
            return ret;
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }
        Set<Field> fds = getAllFields(clazz);
        ret.addAll(fds);
        Class sclazz = clazz.getSuperclass();
        if(sclazz!=null){
            Set<Field> sfds = forceAllFields(sclazz);
            ret.addAll(sfds);
        }
        cacheForceFields.put(clazz, ret);
        return CollectionUtil.toCollection(new HashSet<Field>(),ret);
    }

    /**
     * 检查元素elem是否具有指定的注解clazz
     * ckClass 指定是否检查类上面
     * ckSuperClass 指定是否检查父类上面
     * ckAnnotatedAnn 指定是否检查注解的注解
     *
     * @param elem
     * @param clazz
     * @param ckClass
     * @param ckSuperClass
     * @param ckAnnotatedAnn
     * @return
     */
    public static <T extends Annotation> T findElementAnnotation(AnnotatedElement elem, Class<T> clazz, boolean ckClass, boolean ckSuperClass, boolean ckAnnotatedAnn) {
        if(elem instanceof Class){
            Class cls=(Class)elem;
            if(Object.class.equals(cls)){
                return null;
            }
        }
        T ret = findAnnotation(elem, clazz, ckAnnotatedAnn);
        if (ret != null) {
            return ret;
        }
        Class locClass = null;
        if (elem instanceof AccessibleObject) {
            if (ckClass) {
                if (elem instanceof Field) {
                    Field field = (Field) elem;
                    locClass = field.getDeclaringClass();
                } else if (elem instanceof Method) {
                    Method method = (Method) elem;
                    locClass = method.getDeclaringClass();
                } else if (elem instanceof Constructor) {
                    Constructor cons = (Constructor) elem;
                    locClass = cons.getDeclaringClass();
                }
                ret = findElementAnnotation(locClass, clazz, ckClass, ckSuperClass, ckAnnotatedAnn);
                if (ret != null) {
                    return ret;
                }
            }
        }  else if (elem instanceof Parameter) {
            ret = findAnnotation(elem, clazz, ckAnnotatedAnn);
            if (ret != null) {
                return ret;
            }
        }
        if (ckSuperClass) {
            if (!Object.class.equals(locClass)) {
                Set<Class> supers = new HashSet<>();
                Class superClass = locClass.getSuperclass();
                if(superClass!=null){
                    supers.add(superClass);
                }
                Class[] interfaces = locClass.getInterfaces();
                for (Class item : interfaces) {
                    supers.add(item);
                }
                for (Class item : supers) {
                    ret = findElementAnnotation(item, clazz, ckClass, ckSuperClass, ckAnnotatedAnn);
                    if (ret != null) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }

    public static <T extends Annotation> Map<T,List<Annotation>> findAnnotationsWithSource(AnnotatedElement elem, Class<T> clazz, boolean ckAnnotatedAnn) {
        Set<Annotation> anns = getAllAnnotations(elem);
        Map<T,List<Annotation>> ret = new HashMap<>();
        for (Annotation item : anns) {
            List<Annotation> trace=new ArrayList<>();
            if (item.annotationType().equals(clazz)) {
                ret.put((T)item,trace);
            }
            if (ckAnnotatedAnn) {
                trace.add(item);
                Class type = item.annotationType();
                Map<Annotation,List<Annotation>> superAnns = getAnnotatedAnnotationsNextWithSource(type,trace);
                for (Annotation sitem : superAnns.keySet()) {
                    if (sitem.annotationType().equals(clazz)) {
                        ret.put((T) sitem,superAnns.get(sitem));
                    }
                }
            }
        }
        return ret;
    }

    public static <T extends Annotation> Set<T> findAnnotations(AnnotatedElement elem, Class<T> clazz, boolean ckAnnotatedAnn) {
        Set<Annotation> anns = getAllAnnotations(elem);
        Set<T> ret = new HashSet<>();
        for (Annotation item : anns) {
            if (item.annotationType().equals(clazz)) {
                ret.add((T)item);
            }
            if (ckAnnotatedAnn) {
                Class type = item.annotationType();
                Set<Annotation> superAnns = getAnnotatedAnnotationsNext(type);
                for (Annotation sitem : superAnns) {
                    if (sitem.annotationType().equals(clazz)) {
                        ret.add((T) sitem);
                    }
                }
            }
        }
        return ret;
    }

    public static <T extends Annotation> T findAnnotation(AnnotatedElement elem, Class<T> clazz, boolean ckAnnotatedAnn) {
        Set<Annotation> anns = getAllAnnotations(elem);
        T ret = null;
        for (Annotation item : anns) {
            if (item.annotationType().equals(clazz)) {
                return (T) item;
            }
            if (ckAnnotatedAnn) {
                Class type = item.annotationType();
                Set<Annotation> superAnns = getAnnotatedAnnotationsNext(type);
                for (Annotation sitem : superAnns) {
                    if (sitem.annotationType().equals(clazz)) {
                        return (T) sitem;
                    }
                }
            }
        }
        return ret;
    }

    public static Set<Annotation> getAnnotatedAnnotationsNext(Class<? extends Annotation> annClazz) {
        Set<Annotation> ret = new HashSet<Annotation>();
        Set<Annotation> anns = getAllAnnotations(annClazz);
        for (Annotation item : anns) {
            String annName = item.annotationType().getName();
            if (annName.startsWith("java.lang.annotation.")) {
                continue;
            }
            if(item.annotationType().equals(annClazz)){
                ret.add(item);
                continue;
            }
            ret.add(item);
            Set<Annotation> annotatedAnns = getAnnotatedAnnotationsNext(item.annotationType());
            ret.addAll(annotatedAnns);
        }
        return ret;
    }

    public static Map<Annotation,List<Annotation>> getAnnotatedAnnotationsNextWithSource(Class<? extends Annotation> annClazz,List<Annotation> trace) {
        Map<Annotation,List<Annotation>> ret = new HashMap<Annotation,List<Annotation>>();
        Set<Annotation> anns = getAllAnnotations(annClazz);
        for (Annotation item : anns) {
            String annName = item.annotationType().getName();
            if (annName.startsWith("java.lang.annotation.")) {
                continue;
            }
            if(item.annotationType().equals(annClazz)){
                ret.put(item,new ArrayList<>());
                ret.get(item).addAll(trace);
                continue;
            }
            ret.put(item,new ArrayList<>());
            ret.get(item).addAll(trace);
            ret.get(item).add(item);
            Map<Annotation,List<Annotation>> annotatedAnns = getAnnotatedAnnotationsNextWithSource(item.annotationType(),ret.get(item));
            ret.putAll(annotatedAnns);
        }
        return ret;
    }

    public static Set<Annotation> getAllAnnotations(AnnotatedElement elem) {
        Set<Annotation> ret = new HashSet<>();
        return CollectionUtil.mergeCollection(ret,elem.getAnnotations(),elem.getDeclaredAnnotations());
    }


    public static<T> Constructor<T> findConstructor(Class<T> clazz,Object ... args){
        if(clazz==null){
            return null;
        }
        Set<Constructor> list=getAllConstructors(clazz);
        Constructor cons=null;
        for(Constructor item : list){
            Class[] types=item.getParameterTypes();
            if(canAdaptArgs(types,args)){
                cons=item;
                break;
            }
        }
        return cons;
    }

    public static<T> T instance(Class<T> clazz, Object ... args){
        Constructor<T> cons=findConstructor(clazz,args);
        if(cons==null){
            throw new MethodNotFoundException("not found constructor match args of class:"+clazz.getName());
        }
        try{
            return (T)cons.newInstance(args);
        }catch(Exception e){
            throw new InstanceException("instance class{"+clazz.getName()+"} by constructor("+cons+") failure.");
        }
    }

    public static boolean canAdaptArgs(Class[] targetTypes,Object[] args){
        if(targetTypes.length==0 && targetTypes.length==args.length){
            return true;
        }
        if(targetTypes.length==0 && args.length!=0){
            return false;
        }
        Class[] types=targetTypes;
        Class[] argsTypes=new Class[args.length];
        for(int i=0;i<args.length;i+=1){
            if(args[i]!=null){
                argsTypes[i]=args[i].getClass();
            }else{
                argsTypes[i]=null;
            }
        }
        int compareTypeCount= types.length;
        boolean isVarList=false;
        //变长参数
        if(types[types.length-1].isArray()){
            compareTypeCount--;
            isVarList=true;
        }
        if(args.length<compareTypeCount){
            return false;
        }
        boolean canAdapt=true;
        for(int i=0;i<compareTypeCount;i++){
            if(!ConvertResolver.isConvertible(types[i],argsTypes[i])){
                canAdapt=false;
                break;
            }
        }
        if(canAdapt){
            if(args.length==compareTypeCount){
                return true;
            }else{
                if(isVarList){
                    boolean canVar=true;
                    Class varType=types[types.length-1];
                    for (int i = compareTypeCount; i < args.length; i++) {
                        if(!ConvertResolver.isConvertible(varType,argsTypes[i])){
                            canVar=false;
                            break;
                        }
                    }
                    if(canVar){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Set<Method> findMethodsByName(Class clazz,String methodName){
        Set<Method> ret=new HashSet<>();
        Set<Method> methods=getAllMethods(clazz);
        for(Method item : methods){
            if(item.getName().equals(methodName)){
                ret.add(item);
            }
        }
        return ret;
    }

    public static<T> T invoke(Object ivkObj,String methodName,Object ... args){
        if(ivkObj==null){
            return null;
        }
        Class clazz=ivkObj.getClass();
        Method method=findMethod(clazz,methodName,args);
        if(method==null){
            throw new MethodNotFoundException("not found method ["+methodName+"] in class "+clazz.getName());
        }
        try{
            return (T)method.invoke(ivkObj,args);
        }catch(Exception e){
            throw new MethodAccessFoundException(e);
        }
    }

    public static<T> T staticInvoke(Class clazz,String methodName,Object ... args){
        Method method=findMethod(clazz,methodName,args);
        if(method==null){
            throw new MethodNotFoundException("not found method ["+methodName+"] in class "+clazz.getName());
        }
        if(!Modifier.isStatic(method.getModifiers())){
            throw new MethodAccessFoundException("wait a static method ,but found is not.");
        }
        try{
            return (T)method.invoke(null,args);
        }catch(Exception e){
            throw new MethodAccessFoundException(e);
        }
    }

    public static Method findMethod(Class clazz,String methodName,Object ... args){
        Set<Method> list=findMethodsByName(clazz,methodName);
        if(list.size()==0){
            throw new MethodNotFoundException("wait least one method but found "+list.size());
        }
        if(list.size()==1){
            return list.iterator().next();
        }

        Method method=null;
        for(Method item : list){
            Class[] types=item.getParameterTypes();
            if(canAdaptArgs(types,args)){
                method=item;
                break;
            }
        }
        return method;
    }


    public static Method matchMethod(Class clazz, String methodName, Class... paramTypes) {
        if (CheckUtil.isNull(clazz) || CheckUtil.isEmptyStr(methodName, false)) {
            return null;
        }
        Method ret = null;
        Set<Method> methods = findMethodsByName(clazz,methodName);
        for (Method method : methods) {
            boolean matched = true;
            Class<?>[] types = method.getParameterTypes();
            if (types.length != paramTypes.length) {
                continue;
            }
            for (int i = 0; i < types.length; i++) {
                if (!ConvertResolver.isInTypes(types[i], paramTypes[i])) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                ret = method;
                break;
            }
        }
        if(ret==null){
            try{
                ret=clazz.getMethod(methodName,paramTypes);
            }catch(Exception e){

            }
        }
        return ret;
    }

    public static Set<Method> getMethodsWithAnnotations(Class clazz,boolean ckAnnotatedAnn,Class<? extends Annotation> ... annTypes) {
        Set<Method> ret=new HashSet<>();
        if(annTypes==null || annTypes.length==0){
            return ret;
        }
        Set<Method> list=getAllMethods(clazz);
        for(Method item : list){
            for(Class ann : annTypes){
                Annotation tag=ReflectResolver.findAnnotation(item, ann,ckAnnotatedAnn);
                if(tag!=null){
                    ret.add(item);
                    break;
                }
            }
        }
        return ret;
    }
}
