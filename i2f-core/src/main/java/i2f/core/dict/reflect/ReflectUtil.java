package i2f.core.dict.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Ice2Faith
 * @date 2023/2/21 15:58
 * @desc
 */
public class ReflectUtil {

    public static Set<Field> getFields(Class<?> clazz) {
        Set<Field> ret = new LinkedHashSet<>();
        for (Field item : clazz.getFields()) {
            ret.add(item);
        }
        for (Field item : clazz.getDeclaredFields()) {
            ret.add(item);
        }
        Class<?> superClass = clazz.getSuperclass();
        if (!Object.class.equals(superClass)) {
            Set<Field> superFields = getFields(superClass);
            ret.addAll(superFields);
        }
        return ret;
    }

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> clazz) {
        T ann = elem.getAnnotation(clazz);
        if (ann == null) {
            ann = elem.getDeclaredAnnotation(clazz);
        }
        return ann;
    }
}
