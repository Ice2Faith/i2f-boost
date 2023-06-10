package i2f.liteflow.spel;

/**
 * @author ltb
 * @date 2022/10/14 11:07
 * @desc
 */
public class SpelEnhancer {

    public Integer toInt(Object val) {
        if (val == null) {
            return null;

        }
        if (val instanceof Integer) {
            return (Integer) val;
        }
        try {
            return Integer.parseInt(String.valueOf(val));
        } catch (Exception e) {

        }
        return null;
    }

    public Long toLong(Object val) {
        if (val == null) {
            return null;

        }
        if (val instanceof Long) {
            return (Long) val;
        }
        try {
            return Long.parseLong(String.valueOf(val));
        } catch (Exception e) {

        }
        return null;
    }

    public boolean toBool(Object val) {
        if (val == null) {
            return false;

        }
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        try {
            return Boolean.parseBoolean(String.valueOf(val).toLowerCase());
        } catch (Exception e) {

        }
        return false;
    }

    public String toStr(Object val) {
        if (val == null) {
            return null;

        }
        if (val instanceof String) {
            return (String) val;
        }
        return String.valueOf(val);
    }

    public boolean isNull(Object val) {
        return val == null;
    }

    public boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public boolean contains(String str, String sub) {
        if (str != null && sub != null) {
            return str.contains(sub);
        }
        return false;
    }

    public boolean startWith(String str, String sub) {
        if (str != null && sub != null) {
            return str.startsWith(sub);
        }
        return false;
    }

    public boolean endWith(String str, String sub) {
        if (str != null && sub != null) {
            return str.endsWith(sub);
        }
        return false;
    }

    public boolean match(String str, String sub) {
        if (str != null && sub != null) {
            return str.matches(sub);
        }
        return false;
    }
}
