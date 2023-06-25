package i2f.core.reflection.reflect;

import i2f.core.container.map.Maps;
import i2f.core.type.tuple.Tuples;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;


public class Signatures {
    public static final Map<String, Class> simpleSignaturesNameTypeMap = Collections.unmodifiableMap(Maps.concurrentHashMap(
            Tuples.of("Z", boolean.class),
            Tuples.of("B", byte.class),
            Tuples.of("C", char.class),
            Tuples.of("S", short.class),
            Tuples.of("I", int.class),
            Tuples.of("J", long.class),
            Tuples.of("F", float.class),
            Tuples.of("D", double.class),
            Tuples.of("V", void.class)
    ));
    public static final Map<Class, String> simpleSignaturesTypeNameMap = Collections.unmodifiableMap(Maps.swapKv(simpleSignaturesNameTypeMap, Maps.concurrentHashMap(), null));

    public static <T> String toSign(Class<T> clazz) {
        String sign = simpleSignaturesTypeNameMap.get(clazz);
        if (sign == null) {
            if (clazz.isArray()) {
                sign = clazz.getName();
            } else {
                String name = clazz.getName();
                String path = Reflects.className2UrlPath(name);
                sign = "L" + path + ";";
            }
        }
        return sign;
    }

    public static Class ofSign(String sign) {
        Class clazz = simpleSignaturesNameTypeMap.get(sign);
        if (clazz == null) {
            if (sign.startsWith("[")) {
                clazz = Reflects.findClass(sign);
            } else {
                if (sign.startsWith("L")) {
                    sign = sign.substring(1);
                }
                if (sign.endsWith(";")) {
                    sign = sign.substring(0, sign.length() - 1);
                }
                String name = Reflects.path2ClassName(sign);
                clazz = Reflects.findClass(name);
            }
        }
        if (clazz == null) {
            clazz = Reflects.findClass(sign);
        }
        return clazz;
    }

    public static String sign(Method method) {
        Parameter[] params = method.getParameters();
        Class<?> retType = method.getReturnType();
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (Parameter param : params) {
            builder.append(toSign(param.getType()));
        }
        builder.append(")");
        builder.append(toSign(retType));
        return builder.toString();
    }

    public static String sign(Field field) {
        return toSign(field.getType());
    }

}
