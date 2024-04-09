package i2f.sql.bean;

import i2f.sql.annotations.DbIgnore;
import i2f.sql.annotations.DbIgnoreUnAnnotated;
import i2f.sql.annotations.DbName;
import i2f.sql.annotations.DbPrimary;
import i2f.sql.lambda.impl.NameResolver;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author Ice2Faith
 * @date 2024/4/9 15:01
 * @desc
 */
public class BeanResolver {
    private static final ConcurrentHashMap<Class<?>, Map<Field, Class<?>>> classFieldsMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, Set<Field>> dbFieldsMap = new ConcurrentHashMap<>();

    public static Set<Field> getDbFields(Class<?> clazz) {
        if (clazz == null) {
            return new HashSet<>();
        }
        Set<Field> fields = dbFieldsMap.get(clazz);
        if (fields != null) {
            return new LinkedHashSet<>(fields);
        }
        Map<Field, Class<?>> classFields = getFields(clazz);
        DbIgnoreUnAnnotated classAnn = NameResolver.getAnnotation(clazz, DbIgnoreUnAnnotated.class);
        boolean ignoreUnAnnotated = false;
        if (classAnn != null) {
            ignoreUnAnnotated = classAnn.value();
        }

        Set<Field> ret = new LinkedHashSet<>();
        for (Field field : classFields.keySet()) {
            DbIgnore fieldAnn = NameResolver.getAnnotation(field, DbIgnore.class);
            if (fieldAnn != null) {
                if (fieldAnn.value()) {
                    continue;
                }
            }
            if (ignoreUnAnnotated) {
                DbName nameAnn = NameResolver.getAnnotation(field, DbName.class);
                if (nameAnn != null) {
                    ret.add(field);
                    continue;
                }
                DbPrimary primaryAnn = NameResolver.getAnnotation(field, DbPrimary.class);
                if (primaryAnn != null) {
                    ret.add(field);
                    continue;
                }
                continue;
            }
            ret.add(field);
        }

        dbFieldsMap.put(clazz, new LinkedHashSet<>(ret));
        return ret;
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz) {
        if (clazz == null) {
            return new HashMap<>();
        }
        Map<Field, Class<?>> fields = classFieldsMap.get(clazz);
        if (fields != null) {
            return new LinkedHashMap<>(fields);
        }
        fields = getFields(clazz, null);
        classFieldsMap.put(clazz, new LinkedHashMap<>(fields));
        return fields;
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Field> fieldFilter
    ) {
        return getFields(clazz, fieldFilter, false);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Field> fieldFilter,
                                                 boolean matchedOne
    ) {
        return getFields(clazz, null, fieldFilter, matchedOne);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Class<?>> superFilter,
                                                 Predicate<Field> fieldFilter
    ) {
        return getFields(clazz, superFilter, fieldFilter, false);
    }

    public static Map<Field, Class<?>> getFields(Class<?> clazz,
                                                 Predicate<Class<?>> superFilter,
                                                 Predicate<Field> fieldFilter,
                                                 boolean matchedOne
    ) {
        Map<Field, Class<?>> ret = new LinkedHashMap<>();
        if (clazz == null) {
            return ret;
        }
        Set<Field> arr = findField(clazz, fieldFilter, matchedOne);
        for (Field item : arr) {
            ret.put(item, clazz);
            if (matchedOne) {
                return ret;
            }
        }
        if (Object.class.equals(clazz)) {
            return ret;
        }

        Class<?> superclass = clazz.getSuperclass();
        if (superFilter == null || superFilter.test(superclass)) {
            Map<Field, Class<?>> next = getFields(superclass, superFilter, fieldFilter, matchedOne);
            ret.putAll(next);
        }

        return ret;
    }

    public static Set<Field> findField(Class<?> clazz, Predicate<Field> filter) {
        return findField(clazz, filter, false);
    }

    public static Set<Field> findField(Class<?> clazz, Predicate<Field> filter, boolean matchedOne) {
        Set<Field> ret = new LinkedHashSet<>();
        if (clazz == null) {
            return ret;
        }
        mergeArray(ret, filter, matchedOne, clazz.getDeclaredFields(), clazz.getFields());
        return ret;
    }

    public static <T, C extends Collection<T>> C mergeArray(C ret, Predicate<T> filter, T[] arr1, T[] arr2) {
        return mergeArray(ret, filter, false, arr1, arr2);
    }

    public static <T, C extends Collection<T>> C mergeArray(C ret, Predicate<T> filter, boolean matchedOne, T[] arr1, T[] arr2) {
        if (arr1 != null) {
            for (T item : arr1) {
                if (filter == null || filter.test(item)) {
                    ret.add(item);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        }
        if (arr2 != null) {
            for (T item : arr2) {
                if (filter == null || filter.test(item)) {
                    ret.add(item);
                    if (matchedOne) {
                        return ret;
                    }
                }
            }
        }
        return ret;
    }
}
