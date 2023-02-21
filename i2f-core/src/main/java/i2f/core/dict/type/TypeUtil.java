package i2f.core.dict.type;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:22
 * @desc
 */
public class TypeUtil {
    public static boolean instanceOf(Class<?> sub, Class<?> parent) {
        return sub == parent || sub.equals(parent) || parent.isAssignableFrom(sub);
    }

    public static boolean instanceOfAny(Class<?> sub, Class<?>... parents) {
        for (Class<?> parent : parents) {
            if (instanceOf(sub, parent)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isOriginType(Class<?> type) {
        return instanceOfAny(type,
                int.class, long.class, short.class,
                byte.class, char.class,
                float.class, double.class,
                boolean.class);
    }

    public static boolean isDecorateType(Class<?> type) {
        return instanceOfAny(type,
                Integer.class, Long.class, Short.class,
                Byte.class, Character.class,
                Float.class, Double.class,
                Boolean.class);
    }

    public static boolean isBasicType(Class<?> type) {
        return isOriginType(type) || isDecorateType(type) || instanceOfAny(type, String.class);
    }

    public static boolean isArrayType(Class<?> type) {
        return type.isArray();
    }


}
