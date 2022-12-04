package i2f.core.above;

import i2f.core.data.Pair;
import i2f.core.functional.common.IMapper;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author ltb
 * @date 2022/8/13 13:57
 * @desc
 */
public class Util {
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_GBK = "GBK";
    public static final int MATH_SCALE_DEFAULT = 20;

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static <T> T ifNull(T obj, T def) {
        return isNull(obj) ? def : obj;
    }

    public static <T> T ifElse(boolean condition, T obj, T def) {
        return condition ? obj : def;
    }

    public static boolean isEmpty(String str) {
        return isNull(str) || "".equals(str);
    }

    public static String ifEmpty(String str, String def) {
        return isEmpty(str) ? def : str;
    }

    public static boolean isBlank(String str) {
        if (isNull(str)) {
            return true;
        }
        str = str.trim();
        return "".equals(str);
    }

    public static String ifBlank(String str, String def) {
        return isBlank(str) ? def : str;
    }

    public static String trim(String str) {
        if (isNull(str)) {
            return null;
        }
        return str.trim();
    }

    public static boolean isEmpty(Collection<?> col) {
        return isNull(col) || col.size() == 0;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return isNull(map) || map.size() == 0;
    }

    public static <T> boolean isEmpty(T[] arr) {
        return isNull(arr) || arr.length == 0;
    }

    public static boolean isArray(Object obj) {
        if (isNull(obj)) {
            return false;
        }
        Class clazz = obj.getClass();
        return clazz.isArray();
    }

    public static boolean isEmpty(Object obj) {
        if (isNull(obj)) {
            return true;
        }
        if (isArray(obj)) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof String) {
            return isEmpty((String) obj);
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).size() == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).size() == 0;
        }
        return false;
    }

    public static Integer parseInteger(Object obj, Integer def) {
        try {
            return Integer.parseInt(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static Long parseLong(Object obj, Long def) {
        try {
            return Long.parseLong(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static Double parseDouble(Object obj, Double def) {
        try {
            return Double.parseDouble(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static Float parseFloat(Object obj, Float def) {
        try {
            return Float.parseFloat(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static Boolean parseBoolean(Object obj, Boolean def) {
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static Date parseDate(Object obj, String patten, Date def) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat(patten);
            return fmt.parse(String.valueOf(obj));
        } catch (Exception e) {

        }
        return def;
    }

    public static byte[] string2Bytes(String str, String charset) {
        try {
            return str.getBytes(charset);
        } catch (Exception e) {

        }
        return new byte[0];
    }

    public static String bytes2String(byte[] bytes, String charset) {
        try {
            return new String(bytes, charset);
        } catch (Exception e) {

        }
        return null;
    }

    public static byte[] string2Utf8Bytes(String str) {
        return string2Bytes(str, CHARSET_UTF_8);
    }

    public static String utf8Bytes2String(byte[] bytes) {
        return bytes2String(bytes, CHARSET_UTF_8);
    }

    public static byte[] string2GbkBytes(String str) {
        return string2Bytes(str, CHARSET_GBK);
    }

    public static String gbkBytes2String(byte[] bytes) {
        return bytes2String(bytes, CHARSET_GBK);
    }

    public static <T extends Comparable<T>> int compare(T t1, T t2) {
        if (t1 == t2) {
            return 0;
        }
        if (t1 == null) {
            return 1;
        }
        if (t2 == null) {
            return -1;
        }
        return t1.compareTo(t2);
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

    public static <T extends Comparable<T>> T min(T... objs) {
        if (objs.length == 0) {
            return null;
        }
        T val = objs[0];
        for (int i = 1; i < objs.length; i++) {
            if (compare(objs[i], val) < 0) {
                val = objs[i];
            }
        }
        return val;
    }

    public static <T extends Comparable<T>> T max(T... objs) {
        if (objs.length == 0) {
            return null;
        }
        T val = objs[0];
        for (int i = 1; i < objs.length; i++) {
            if (compare(objs[i], val) > 0) {
                val = objs[i];
            }
        }
        return val;
    }

    public static String dateFormat(Date date, String patten) {
        SimpleDateFormat fmt = new SimpleDateFormat(patten);
        return fmt.format(date);
    }

    public static String dateFormat(LocalDate date, String patten) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
        return date.format(fmt);
    }

    public static String dateFormat(LocalDateTime date, String patten) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
        return date.format(fmt);
    }

    public static String dateFormat(LocalTime date, String patten) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern(patten);
        return date.format(fmt);
    }

    public static String stringFormat(String format, Object... vals) {
        return String.format(format, vals);
    }

    public static int hashCapital(int size) {
        if (size < 32) {
            return 32;
        }
        return (int) (size * 0.75);
    }

    public static <E, IN extends Enumeration<E>, OUT extends Collection<E>> OUT copyCollection(IN src, OUT dst) {
        while (src.hasMoreElements()) {
            dst.add(src.nextElement());
        }
        return dst;
    }

    public static <E, IN extends Iterator<E>, OUT extends Collection<E>> OUT copyCollection(IN src, OUT dst) {
        while (src.hasNext()) {
            dst.add(src.next());
        }
        return dst;
    }

    public static <E, IN extends Iterable<E>, OUT extends Collection<E>> OUT copyCollection(IN src, OUT dst) {
        Iterator<E> iterator = src.iterator();
        return copyCollection(iterator, dst);
    }

    public static <E, IN extends Collection<E>, OUT extends Collection<E>> OUT copyCollection(IN src, OUT dst) {
        for (E item : src) {
            dst.add(item);
        }
        return dst;
    }

    public static <T> HashSet<T> ofHashSet(Collection<T> col) {
        HashSet<T> ret = new HashSet<>(hashCapital(col.size()));
        return copyCollection(col, ret);
    }

    public static <T extends Comparable<T>> TreeSet<T> ofTreeSet(Collection<T> col) {
        TreeSet<T> ret = new TreeSet<>();
        return copyCollection(col, ret);
    }

    public static <T> TreeSet<T> ofTreeSet(Collection<T> col, Comparator<T> comparator) {
        TreeSet<T> ret = new TreeSet<>(comparator);
        return copyCollection(col, ret);
    }

    public static <T> LinkedHashSet<T> ofLinkedHashSet(Collection<T> col) {
        LinkedHashSet<T> ret = new LinkedHashSet<>(hashCapital(col.size()));
        return copyCollection(col, ret);
    }

    public static <T> CopyOnWriteArraySet<T> ofCopyOnWriteArraySet(Collection<T> col) {
        return new CopyOnWriteArraySet<>(col);
    }

    public static <T> ArrayList<T> ofArrayList(Collection<T> col) {
        ArrayList<T> ret = new ArrayList<>(col.size());
        return copyCollection(col, ret);
    }

    public static <T> CopyOnWriteArrayList<T> ofCopyOnWriteArrayList(Collection<T> col) {
        return new CopyOnWriteArrayList<>(col);
    }

    public static <T> LinkedList<T> ofLinkedList(Collection<T> col) {
        LinkedList<T> ret = new LinkedList<>();
        return copyCollection(col, ret);
    }

    public static <T, K, V, IN extends Collection<T>, OUT extends Map<K, V>> OUT copyMap(IN src, OUT dst, IMapper<Pair<K, V>, T> mapper) {
        for (T item : src) {
            Pair<K, V> pair = mapper.get(item);
            dst.put(pair.key, pair.val);
        }
        return dst;
    }

    public static <T, K, V> HashMap<K, V> ofHashMap(Collection<T> col, IMapper<Pair<K, V>, T> mapper) {
        HashMap<K, V> ret = new HashMap<>(hashCapital(col.size()));
        return copyMap(col, ret, mapper);
    }

    public static <T, K, V> LinkedHashMap<K, V> ofLinkedHashMap(Collection<T> col, IMapper<Pair<K, V>, T> mapper) {
        LinkedHashMap<K, V> ret = new LinkedHashMap<>(hashCapital(col.size()));
        return copyMap(col, ret, mapper);
    }

    public static <T, K extends Comparable<K>, V> TreeMap<K, V> ofTreeMap(Collection<T> col, IMapper<Pair<K, V>, T> mapper) {
        TreeMap<K, V> ret = new TreeMap<>();
        return copyMap(col, ret, mapper);
    }

    public static <T, K, V> TreeMap<K, V> ofTreeMap(Collection<T> col, IMapper<Pair<K, V>, T> mapper, Comparator<K> comparator) {
        TreeMap<K, V> ret = new TreeMap<>(comparator);
        return copyMap(col, ret, mapper);
    }

    public static <T, K, V> ConcurrentHashMap<K, V> ofConcurrentHashMap(Collection<T> col, IMapper<Pair<K, V>, T> mapper) {
        ConcurrentHashMap<K, V> ret = new ConcurrentHashMap<>(hashCapital(col.size()));
        return copyMap(col, ret, mapper);
    }

    public static <K, V, IN extends Map<K, V>, OUT extends Map<K, V>> OUT copyMap(IN src, OUT dst) {
        for (Map.Entry<K, V> item : src.entrySet()) {
            dst.put(item.getKey(), item.getValue());
        }
        return dst;
    }


    public static <K, V, MAP extends Map<K, V>> MAP ofMap(MAP map, Pair<K, V>... pairs) {
        for (Pair<K, V> item : pairs) {
            map.put(item.key, item.val);
        }
        return map;
    }

    public static <K, V> HashMap<K, V> ofHashMap(Pair<K, V>... pairs) {
        HashMap<K, V> map = new HashMap<>(hashCapital(pairs.length));
        return ofMap(map, pairs);
    }

    public static <K, V> LinkedHashMap<K, V> ofLinkedHashMap(Pair<K, V>... pairs) {
        LinkedHashMap<K, V> map = new LinkedHashMap<>(hashCapital(pairs.length));
        return ofMap(map, pairs);
    }

    public static <K extends Comparable<K>, V> TreeMap<K, V> ofTreeMap(Pair<K, V>... pairs) {
        TreeMap<K, V> map = new TreeMap<>();
        return ofMap(map, pairs);
    }

    public static <K, V> TreeMap<K, V> ofTreeMap(Comparator<K> comparator, Pair<K, V>... pairs) {
        TreeMap<K, V> map = new TreeMap<>(comparator);
        return ofMap(map, pairs);
    }

    public static <K, V> ConcurrentHashMap<K, V> ofConcurrentHashMap(Pair<K, V>... pairs) {
        ConcurrentHashMap<K, V> map = new ConcurrentHashMap<>(hashCapital(pairs.length));
        return ofMap(map, pairs);
    }

    public static <T> T[] copyArray(Collection<T> col, Class<? extends T[]> tarType) {
        Object[] arr = new Object[col.size()];
        Iterator it = col.iterator();
        int ix = 0;
        while (it.hasNext()) {
            arr[ix] = it.next();
            ix++;
        }
        return (T[]) Arrays.copyOf(arr, arr.length, tarType);
    }

    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v2);
    }

    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v2);
    }

    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        return v1.multiply(v2);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale) {
        return v1.divide(v2, scale, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal div(BigDecimal v1, BigDecimal v2) {
        return div(v1, v2, MATH_SCALE_DEFAULT);
    }

    public static BigInteger add(BigInteger v1, BigInteger v2) {
        return v1.add(v2);
    }

    public static BigInteger sub(BigInteger v1, BigInteger v2) {
        return v1.subtract(v2);
    }

    public static BigInteger mul(BigInteger v1, BigInteger v2) {
        return v1.multiply(v2);
    }

    public static BigInteger div(BigInteger v1, BigInteger v2) {
        return v1.divide(v2);
    }

    public static Thread currentThread() {
        return Thread.currentThread();
    }

    public static long currentThreadId() {
        return currentThread().getId();
    }

    public static String currentThreadName() {
        return currentThread().getName();
    }

    public static ClassLoader currentClassLoader() {
        return currentThread().getContextClassLoader();
    }

    public static URL getResource(String name) {
        return currentClassLoader().getResource(name);
    }

    public static InputStream getResourceAsStream(String name) {
        return currentClassLoader().getResourceAsStream(name);
    }

    public static List<URL> getResources(String name) throws IOException {
        Enumeration<URL> enumeration = currentClassLoader().getResources(name);
        LinkedList<URL> list = new LinkedList<>();
        return copyCollection(enumeration, list);
    }

    public static Runtime runtime() {
        return Runtime.getRuntime();
    }

    public static int processorCount() {
        return runtime().availableProcessors();
    }

    public static long freeMemory() {
        return runtime().freeMemory();
    }

    public static long totalMemory() {
        return runtime().totalMemory();
    }

    public static long maxMemory() {
        return runtime().maxMemory();
    }

    public static void shutdownHook(Runnable runnable) {
        runtime().addShutdownHook(new Thread(runnable));
    }

    public static void loadLibByPath(String absolutePath) {
        runtime().load(absolutePath);
    }

    public static void loadLibByEnv(String libName) {
        runtime().loadLibrary(libName);
    }

    public static Class<?> findClass(String fullClassName) {
        try {
            Class<?> clazz = Class.forName(fullClassName);
            return clazz;
        } catch (Exception e) {
        }
        try {
            Class<?> clazz = currentClassLoader().loadClass(fullClassName);
            return clazz;
        } catch (Exception e) {

        }
        return null;
    }

    public static boolean existsClass(String fullClassName) {
        return findClass(fullClassName) != null;
    }

    public static String getProperty(String name) {
        return System.getProperty(name);
    }

    public static Integer getIntegerProperty(String name, Integer def) {
        return parseInteger(getProperty(name), def);
    }

    public static Double getDoubleProperty(String name, Double def) {
        return parseDouble(getProperty(name), def);
    }

    public static Float getFloatProperty(String name, Float def) {
        return parseFloat(getProperty(name), def);
    }

    public static Long getLongProperty(String name, Long def) {
        return parseLong(getProperty(name), def);
    }

    public static Boolean getBooleanProperty(String name, Boolean def) {
        return parseBoolean(getProperty(name), def);
    }
}
