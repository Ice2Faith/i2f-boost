package i2f.core.safe;

public class Safes {
    public static <T extends Comparable<T>> int compare(T t1, T t2) {
        if (t1 == t2) {
            return 0;
        }
        if (t1 == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t1.compareTo(t2);
    }

    public static <T> int equalCompare(T t1, T t2) {
        if (t1 == null && t2 == null) {
            return 0;
        }
        if (t1 == null) {
            return 1;
        }
        if (t2 == null) {
            return -1;
        }
        if (t1.equals(t2)) {
            return 0;
        }
        return 1;
    }

    public static <T extends Comparable<T>> boolean gt(T t1, T t2) {
        return compare(t1, t2) > 0;
    }

    public static <T extends Comparable<T>> boolean lt(T t1, T t2) {
        return compare(t1, t2) < 0;
    }

    public static <T extends Comparable<T>> boolean gte(T t1, T t2) {
        return compare(t1, t2) >= 0;
    }

    public static <T extends Comparable<T>> boolean lte(T t1, T t2) {
        return compare(t1, t2) <= 0;
    }

    public static <T extends Comparable<T>> boolean eq(T t1, T t2) {
        return compare(t1, t2) == 0;
    }

    public static <T extends Comparable<T>> boolean neq(T t1, T t2) {
        return compare(t1, t2) != 0;
    }

    public static <T extends Comparable<T>> T min(T v1, T v2) {
        int cp = compare(v1, v2);
        if (cp <= 0) {
            return v1;
        }
        return v2;
    }

    public static <T extends Comparable<T>> T max(T v1, T v2) {
        int cp = compare(v1, v2);
        if (cp >= 0) {
            return v1;
        }
        return v2;
    }

    public static <T> boolean in(T val, T... cmps) {
        for (T item : cmps) {
            if (val == item) {
                return true;
            }
            if (val != null
                    && item != null
                    && val.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public static Long parseLong(Object object, Long defVal) {
        if (object instanceof Long) {
            return (Long) object;
        }
        String str = String.valueOf(object);
        try {
            return Long.parseLong(str);
        } catch (Exception e) {

        }
        return defVal;
    }

    public static Integer parseInt(Object object, Integer defVal) {
        if (object instanceof Integer) {
            return (Integer) object;
        }
        String str = String.valueOf(object);
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {

        }
        return defVal;
    }

    public static Double parseDouble(Object object, Double defVal) {
        if (object instanceof Double) {
            return (Double) object;
        }
        String str = String.valueOf(object);
        try {
            return Double.parseDouble(str);
        } catch (Exception e) {

        }
        return defVal;
    }

    public static Float parseFloat(Object object, Float defVal) {
        if (object instanceof Float) {
            return (Float) object;
        }
        String str = String.valueOf(object);
        try {
            return Float.parseFloat(str);
        } catch (Exception e) {

        }
        return defVal;
    }

    public static Short parseShort(Object object, Short defVal) {
        if (object instanceof Short) {
            return (Short) object;
        }
        String str = String.valueOf(object);
        try {
            return Short.parseShort(str);
        } catch (Exception e) {

        }
        return defVal;
    }

    public static Boolean parseBoolean(Object object, Boolean defVal) {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        String str = String.valueOf(object);
        try {
            return Boolean.parseBoolean(str);
        } catch (Exception e) {

        }
        return defVal;
    }


}
