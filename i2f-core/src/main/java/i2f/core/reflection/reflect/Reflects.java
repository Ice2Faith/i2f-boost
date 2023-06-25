package i2f.core.reflection.reflect;

import i2f.core.container.collection.Collections;
import i2f.core.container.map.Maps;
import i2f.core.lang.functional.jvf.Predicate;
import i2f.core.type.str.Strings;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;


public class Reflects {
    /**
     * 路径转类名或包名
     *
     * @param path
     * @return
     */
    public static String path2ClassName(String path) {
        return Strings.replacesAll(path, ".", "\\\\", "/");
    }

    /**
     * 包名或类名转路径
     *
     * @param className
     * @return
     */
    public static String className2Path(String className) {
        return Strings.replacesAll(className, File.separator, "\\.");
    }

    /**
     * 包名或类名转URL的路径，也就是/分隔
     *
     * @param className
     * @return
     */
    public static String className2UrlPath(String className) {
        return Strings.replacesAll(className, "/", "\\.");
    }

    /**
     * 获取上下文类加载器
     *
     * @return
     */
    public static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 根据类名查找类
     * 找不到返回null
     *
     * @param className
     * @return
     */
    public static Class findClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {

        }
        try {
            Class<?> clazz = contextClassLoader().loadClass(className);
            if (clazz != null) {
                return clazz;
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static String fieldNameByMethodName(String methodName) {
        String fieldName = Strings.trimPrefix(methodName, "get", "set", "is", "with", "build");
        return Strings.firstLower(fieldName);
    }

    public static <T> Field field(Class<T> clazz, String name) {
        return field(clazz, name, false);
    }

    public static <T> Field field(Class<T> clazz, String name, boolean recursive) {
        Map<Field, Class> fields = fields(clazz, (e) -> e.getName().equals(name), recursive);
        for (Field item : fields.keySet()) {
            return item;
        }
        return null;
    }

    public static <T> Method method(Class<T> clazz, String name) {
        return method(clazz, name, false);
    }

    public static <T> Method method(Class<T> clazz, String name, boolean recursive) {
        Map<Method, Class> methods = methods(clazz, (e) -> e.getName().equals(name), recursive);
        for (Method item : methods.keySet()) {
            return item;
        }
        return null;
    }

    public static Method methodOfSignature(Class clazz, String methodName, String methodSignature) {
        return methodOfSignature(clazz, methodName, methodSignature, false);
    }

    public static <T> Method methodOfSignature(Class<T> clazz, String methodName, String methodSignature, boolean recursive) {
        Map<Method, Class> methods = methods(clazz, (e) -> e.getName().equals(methodName), recursive);
        for (Method item : methods.keySet()) {
            String sign = Signatures.sign(item);
            if (sign.equals(methodSignature)) {
                return item;
            }
        }
        return null;
    }

    public static <T> Set<Field> fields(Class<T> clazz, Predicate<Field> filter) {
        LinkedHashSet<Field> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        Collections.collect(ret, filter, clazz.getFields());
        Collections.collect(ret, filter, clazz.getDeclaredFields());
        return ret;
    }

    public static <T> Set<Method> methods(Class<T> clazz, Predicate<Method> filter) {
        LinkedHashSet<Method> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        Collections.collect(ret, filter, clazz.getMethods());
        Collections.collect(ret, filter, clazz.getDeclaredMethods());
        return ret;
    }

    public static <T> Set<Constructor<T>> constructors(Class<T> clazz, Predicate<Constructor<T>> filter) {
        LinkedHashSet<Constructor<T>> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        Collections.collect(ret, filter, (Constructor<T>[]) clazz.getConstructors());
        Collections.collect(ret, filter, (Constructor<T>[]) clazz.getDeclaredConstructors());
        return ret;
    }

    public static <T> Map<Field, Class> fields(Class<T> clazz, Predicate<Field> filter, boolean recursive) {
        LinkedHashMap<Field, Class> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Field> fields = fields(clazz, filter);
        Maps.valueAs(ret, clazz, fields.iterator());
        if (recursive) {
            if (!Object.class.equals(clazz)) {
                Class<? super T> superClass = clazz.getSuperclass();
                if (superClass != null) {
                    Map<Field, Class> next = fields(superClass, filter, recursive);
                    ret.putAll(next);
                }
            }
        }
        return ret;
    }

    public static <T> Map<Method, Class> methods(Class<T> clazz, Predicate<Method> filter, boolean recursive) {
        LinkedHashMap<Method, Class> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Method> methods = methods(clazz, filter);
        Maps.valueAs(ret, clazz, methods.iterator());
        if (recursive) {
            if (!Object.class.equals(clazz)) {
                Class<? super T> superClass = clazz.getSuperclass();
                Class<?>[] interfaces = clazz.getInterfaces();
                if (superClass != null) {
                    Map<Method, Class> next = methods(superClass, filter, recursive);
                    ret.putAll(next);
                }
                for (Class item : interfaces) {
                    Map<Method, Class> next = methods(item, filter, recursive);
                    ret.putAll(next);
                }
            }
        }
        return ret;
    }

    public static <T extends Annotation> T annotations(AnnotatedElement elem, Class<T> annClass) {
        Set<Annotation> annotations = annotations(elem, (e) -> e.annotationType().equals(annClass));
        for (Annotation item : annotations) {
            return (T) item;
        }
        return null;
    }

    public static <T extends Annotation> T annotations(Class elem, Class<T> annClass, boolean recursive) {
        Set<Annotation> annotations = annotations(elem, (e) -> e.annotationType().equals(annClass), recursive);
        for (Annotation item : annotations) {
            return (T) item;
        }
        return null;
    }

    public static <T extends Annotation> T annotations(Member elem, Class<T> annClass, boolean searchClass, boolean recursive) {
        Set<Annotation> annotations = annotations(elem, (e) -> e.annotationType().equals(annClass), searchClass, recursive);
        for (Annotation item : annotations) {
            return (T) item;
        }
        return null;
    }

    public static Set<Annotation> annotations(AnnotatedElement elem, Predicate<Annotation> filter) {
        LinkedHashSet<Annotation> ret = new LinkedHashSet<>();
        if (elem == null) {
            return ret;
        }
        Collections.collect(ret, filter, elem.getAnnotations());
        Collections.collect(ret, filter, elem.getDeclaredAnnotations());
        return ret;
    }

    public static Set<Annotation> annotations(Class clazz, Predicate<Annotation> filter, boolean recursive) {
        LinkedHashSet<Annotation> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        ret.addAll(annotations(clazz, filter));
        if (recursive) {
            if (!Object.class.equals(clazz)) {
                Class<?> superClass = clazz.getSuperclass();
                Class<?>[] interfaces = clazz.getInterfaces();
                if (superClass != null) {
                    Set<Annotation> next = annotations(superClass, filter, recursive);
                    ret.addAll(next);
                }
                for (Class item : interfaces) {
                    Set<Annotation> next = annotations(item, filter, recursive);
                    ret.addAll(next);
                }
            }
        }
        return ret;
    }

    public static Set<Annotation> annotations(Member mem, Predicate<Annotation> filter, boolean searchClass, boolean recursive) {
        LinkedHashSet<Annotation> ret = new LinkedHashSet<>();
        if (mem == null) {
            return ret;
        }
        if (mem instanceof AnnotatedElement) {
            AnnotatedElement elem = (AnnotatedElement) mem;
            ret.addAll(annotations(elem, filter));
        }
        if (searchClass) {
            Class<?> declaringClass = mem.getDeclaringClass();
            ret.addAll(annotations(declaringClass, filter, recursive));
        }
        return ret;
    }


    public static <T> Method getter(Class<T> clazz, String name) {
        String[] getterPrefixes = {"get", "is"};
        String camelName = Strings.firstUpper(name);
        for (String prefix : getterPrefixes) {
            String methodName = prefix + camelName;
            Set<Method> methods = methods(clazz, (e) -> e.getName().equals(methodName)
                    && Modifier.isPublic(e.getModifiers())
                    && !e.getReturnType().equals(Void.class) && e.getParameterCount() == 0
            );
            for (Method item : methods) {
                return item;
            }
        }
        return null;
    }

    public static <T> Method setter(Class<T> clazz, String name) {
        String[] getterPrefixes = {"set", "build", "with"};
        String camelName = Strings.firstUpper(name);
        for (String prefix : getterPrefixes) {
            String methodName = prefix + camelName;
            Set<Method> methods = methods(clazz, (e) -> e.getName().equals(methodName)
                    && Modifier.isPublic(e.getModifiers())
                    && e.getParameterCount() == 1
            );
            for (Method item : methods) {
                return item;
            }
        }
        return null;
    }

    public static <T> T get(Object ivkObj, String name) {
        Class clazz = ivkObj.getClass();
        try {
            Method method = getter(clazz, name);
            if (method != null) {
                return (T) method.invoke(ivkObj);
            }
            Field field = field(clazz, name, true);
            field.setAccessible(true);
            return (T) field.get(ivkObj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static <T> void set(Object ivkObj, String name, T value) {
        Class clazz = ivkObj.getClass();
        try {
            Method method = setter(clazz, name);
            if (method != null) {
                method.invoke(ivkObj, value);
                return;
            }
            Field field = field(clazz, name, true);
            field.setAccessible(true);
            field.set(ivkObj, value);
            return;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static <T> T get(Field field, Object ivkObj) {
        Class clazz = ivkObj.getClass();
        try {
            String name = field.getName();
            Method method = getter(clazz, name);
            if (method != null) {
                return (T) method.invoke(ivkObj);
            }
            field.setAccessible(true);
            return (T) field.get(ivkObj);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static <T> void set(Field field, Object ivkObj, T value) {
        Class clazz = ivkObj.getClass();
        try {
            String name = field.getName();
            Method method = setter(clazz, name);
            if (method != null) {
                method.invoke(ivkObj, value);
                return;
            }
            field.setAccessible(true);
            field.set(ivkObj, value);
            return;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public static boolean isTypeOf(Class clazz, Class type) {
        if (clazz == null || type == null) {
            return false;
        }
        return clazz.equals(type) || type.isAssignableFrom(clazz);
    }

    public static boolean instanceOf(Object obj, Class type) {
        if (obj == null) {
            return isTypeOf(null, type);
        }
        return isTypeOf(obj.getClass(), type);
    }

    public static <T> T instance(Class<T> clazz) {
        Set<Constructor<T>> constructors = constructors(clazz, (e) -> {
            return Modifier.isPublic(e.getModifiers()) && e.getParameterCount() == 0;
        });
        Iterator<Constructor<T>> iterator = constructors.iterator();
        try {
            if (iterator.hasNext()) {
                Constructor<T> next = iterator.next();
                return next.newInstance();
            }
            return clazz.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
