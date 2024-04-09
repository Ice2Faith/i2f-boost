package i2f.sql.lambda.impl;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.Vector;

/**
 * @author Ice2Faith
 * @date 2024/4/9 14:04
 * @desc
 */
public class NameResolver {

    public static <T extends Annotation> T getAnnotation(AnnotatedElement elem, Class<T> annClass) {
        T ann = null;
        if (ann == null) {
            try {
                ann = elem.getAnnotation(annClass);
            } catch (Exception e) {

            }
        }
        if (ann == null) {
            try {
                ann = elem.getDeclaredAnnotation(annClass);
            } catch (Exception e) {

            }
        }
        return ann;
    }


    public static String toPascal(String str) {
        if (isEmptyStr(str)) {
            return str;
        }
        if (!str.contains("_") && !str.contains("-")) {
            return firstUpperCase(str);
        }
        String[] arr = split(str, true, "_|-", -1, true);
        StringBuffer buffer = new StringBuffer();
        for (String item : arr) {
            buffer.append(firstUpperCase(item));
        }
        return buffer.toString();
    }

    public static String toCamel(String str) {
        if (isEmptyStr(str)) {
            return str;
        }
        if (!str.contains("_") && !str.contains("-")) {
            return firstLowerCase(str);
        }
        String[] arr = split(str, true, "_|-", -1, true);
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < arr.length; i++) {
            if (i == 0) {
                buffer.append(firstLowerCase(arr[i]));
            } else {
                buffer.append(firstUpperCase(arr[i]));
            }
        }
        return buffer.toString();
    }

    public static String toUnderScore(String str) {
        if (str.contains("_")) {
            return str.trim();
        }
        StringBuffer buffer = new StringBuffer();
        char[] arr = str.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= 'A' && arr[i] <= 'Z') {
                if (i > 0) {
                    buffer.append("_");
                }
                buffer.append((char) (arr[i] | 32));
            } else {
                buffer.append(arr[i]);
            }
        }
        return buffer.toString();
    }

    public static String firstUpperCase(String str) {
        if (isEmptyStr(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String firstLowerCase(String str) {
        if (isEmptyStr(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    public static boolean isEmptyStr(String str) {
        return str == null || "".equals(str);
    }

    public static String[] split(String str, String regex, boolean removeEmpty) {
        return split(str, false, regex, -1, removeEmpty);
    }

    public static String[] split(String str, boolean trimBefore, String regex, int limit, boolean removeEmpty) {
        String[] ret = new String[]{};
        if (str == null) {
            return ret;
        }
        if (trimBefore) {
            str = str.trim();
        }
        ret = str.split(regex, limit);
        Vector<String> result = new Vector<>();
        for (String item : ret) {
            if (removeEmpty) {
                if ("".equals(item)) {
                    continue;
                }
            }
            result.add(item);
        }
        return result.toArray(new String[0]);
    }


}
