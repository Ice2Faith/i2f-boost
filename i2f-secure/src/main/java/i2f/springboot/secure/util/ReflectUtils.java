package i2f.springboot.secure.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Member;

/**
 * @author Ice2Faith
 * @date 2023/6/13 9:53
 * @desc
 */
public class ReflectUtils {
    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> annClass) {
        T ann = elem.getDeclaredAnnotation(annClass);
        if (ann == null) {
            ann = elem.getAnnotation(annClass);
        }
        return ann;
    }

    public static <T extends Annotation> T getMemberAnnotation(Member member, Class<T> annClass) {
        T ann = null;
        if (member instanceof AnnotatedElement) {
            ann = getAnnotation((AnnotatedElement) member, annClass);
        }
        if (ann == null) {
            ann = getAnnotation(member.getDeclaringClass(), annClass);
        }
        return ann;
    }
}
