package i2f.core.jdbc.data;

import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * @author ltb
 * @date 2022/10/11 16:00
 * @desc
 */
@Getter
@Setter
@NoArgsConstructor
public class DBResultList extends LinkedList<Map<String, Object>> {
    protected String[] headers;
    protected Long count;
    protected Long index;
    protected Long limit;

    public Object getObjectValue(int row, int col) {
        return super.get(row).get(this.headers[col]);
    }

    public Object getObjectValue(int row, String col) {
        return super.get(row).get(col);
    }

    public Object getObjectValue() {
        return getObjectValue(0, 0);
    }

    public Long getLongValue() {
        return getLongValue(0, 0);
    }

    public Integer getIntegerValue() {
        return getIntegerValue(0, 0);
    }

    public String getStringValue() {
        return getStringValue(0, 0);
    }

    public Long getLongValue(int row, int col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return Long.parseLong(val + "");
    }

    public Integer getIntegerValue(int row, int col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return Integer.parseInt(val + "");
    }

    public String getStringValue(int row, int col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return String.valueOf(val);
    }

    public Long getLongValue(int row, String col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return Long.parseLong(val + "");
    }

    public Integer getIntegerValue(int row, String col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return Integer.parseInt(val + "");
    }

    public String getStringValue(int row, String col) {
        Object val = getObjectValue(row, col);
        if (val == null) {
            return null;
        }
        return String.valueOf(val);
    }

    public Map<String, Object> getOne() {
        return super.get(0);
    }

    public <T> List<T> parseBean(Class<T> clazz) {
        return parseBean(this, clazz);
    }

    public static DBResultList of(ResultSet rs) throws SQLException {
        return of(rs, null, null, null);
    }

    public static DBResultList of(ResultSet rs, Long count) throws SQLException {
        return of(rs, count, null, null);
    }

    public static DBResultList of(ResultSet rs, Long count, Long index, Long limit) throws SQLException {
        DBResultList ret = new DBResultList();
        ret.headers = new String[0];
        ret.count = 0L;
        ret.index = 0L;
        ret.limit = 0L;
        try {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            ret.headers = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                String colName = meta.getColumnName(i);
                ret.headers[i] = colName;
            }
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= colCount; i++) {
                    String colName = meta.getColumnName(i);
                    Object colValue = rs.getObject(colName);
                    row.put(colName, colValue);
                }
                ret.count++;
                ret.limit++;
                ret.add(row);
            }

        } catch (Exception e) {
            throw new SQLException("read DataSet error", e);
        }
        if (count != null && count >= 0) {
            ret.count = count;
        }
        if (index != null && index >= 0) {
            ret.index = index;
        }
        if (limit != null && limit >= 0) {
            ret.limit = limit;
        }
        return ret;
    }


    /**
     * 宽松匹配字段映射
     * 将给定的列名，映射为目标bean的字段名
     * 使用宽松比较的情况下，将会优先equals,其次是ignoreEquals,最后是忽略所有符号的仅英文字符的匹配
     * 也就是说，如下情况将都会匹配到：
     * java字段：userId
     * 列名可以是：userId,userid,USERID,user_id,USER_ID,user-ID
     *
     * @param namings   具有的列名
     * @param beanClass 目标bean的类型
     * @return
     */
    public static Map<String, PropertyAccessor> looseNamingMapping(List<String> namings, Class beanClass) {
        List<PropertyAccessor> fields = ReflectResolver.getLogicalReadWriteFields(beanClass);
        Map<String, PropertyAccessor> columnMapping = new HashMap<>(namings.size() * 2);
        for (String col : namings) {
            // 根据匹配权重得到的匹配映射表
            TreeMap<Integer, PropertyAccessor> waitMap = new TreeMap<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return -Integer.compare(o1, o2);
                }
            });
            for (PropertyAccessor accessor : fields) {
                String fieldName = accessor.getName();
                if (col.equals(fieldName)) {
                    waitMap.put(100, accessor); // 100%匹配，完全一致
                } else if (col.equalsIgnoreCase(fieldName)) {
                    waitMap.put(80, accessor); // 80%匹配，忽略大小写一致
                } else {
                    String colAzName = col.replaceAll("_|-", "");
                    String fieldAzName = col.replaceAll("_|-", "");
                    if (colAzName.equals(fieldAzName)) {
                        waitMap.put(60, accessor); // 60%匹配，去除符号后完全匹配
                    } else if (colAzName.equalsIgnoreCase(fieldAzName)) {
                        waitMap.put(50, accessor); // 50%匹配，去除符号后大小写不敏感匹配
                    }
                }
            }
            if (waitMap.size() > 0) { // 根据treemap特性，获取第一个，即为最优匹配项
                PropertyAccessor accessor = waitMap.entrySet().iterator().next().getValue();
                columnMapping.put(col, accessor);
            }
        }
        return columnMapping;
    }

    /**
     * 将结果映射为指定的实体bean
     * 需要指定实体bean类型
     * 并且使用宽松比较
     * 使用宽松比较的情况下，将会优先equals,其次是ignoreEquals,最后是忽略所有符号的仅英文字符的匹配
     * 也就是说，如下情况将都会匹配到：
     * java字段：userId
     * 列名可以是：userId,userid,USERID,user_id,USER_ID,user-ID
     *
     * @param beanClass
     * @param <T>
     * @return
     */
    public static <T> List<T> parseBean(DBResultList list, Class<T> beanClass) {
        Map<String, PropertyAccessor> columnMapping = looseNamingMapping(Arrays.asList(list.getHeaders()), beanClass);
        List<T> ret = new ArrayList<>(list.size());
        for (Map<String, Object> row : list) {
            T obj = ReflectResolver.instance(beanClass);
            for (Map.Entry<String, Object> item : row.entrySet()) {
                String name = item.getKey();
                Object val = item.getValue();
                PropertyAccessor accessor = columnMapping.get(name);
                if (accessor != null) {
                    accessor.setInvokeObject(obj);
                    accessor.set(val);
                }
            }
        }
        return ret;
    }
}
