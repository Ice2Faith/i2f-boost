package i2f.core.container.array;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.check.CheckUtil;
import i2f.core.container.collection.Collections;

import java.lang.reflect.Array;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/22 19:32
 * @desc
 */
@Author("i2f")
@Remark("provide array operation")
public class Arrays {
    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    public static <T> boolean isEmpty(T[] arr) {
        return arr == null || arr.length == 0;
    }

    public static boolean isEmpty(Object obj) {
        return obj == null || length(obj) == 0;
    }

    public static <T> RefArray<T> refArray(Object obj) {
        return new RefArray<T>(obj);
    }

    public static int length(@Name("arr") Object arr) {
        return Array.getLength(arr);
    }

    public static <T> T get(@Name("arr") Object arr, @Name("index") int index) {
        return (T) Array.get(arr, index);
    }

    public static void set(@Name("arr") Object arr, @Name("index") int index, @Name("val") Object val) {
        Array.set(arr, index, val);
    }

    public static <T> T[] collect(@Name("objs") T... objs) {
        return (T[]) objs;
    }


    public static <T> T[] collect(@Name("ite") Enumeration<T> ite, @Name("tarType") Class<? extends T[]> tarType) {
        List<T> list = new ArrayList<>();
        Collections.collect(list, ite);
        return collect(list, tarType);
    }

    public static <T> T[] collect(@Name("ite") Iterable<T> ite, @Name("tarType") Class<? extends T[]> tarType) {
        List<T> list = new ArrayList<>();
        Collections.collect(list, ite);
        return collect(list, tarType);
    }

    public static <T> T[] collect(@Name("ite") Iterator<T> ite, @Name("tarType") Class<? extends T[]> tarType) {
        List<T> list = new ArrayList<>();
        Collections.collect(list, ite);
        return collect(list, tarType);
    }


    public static <T> T[] collect(@Name("col") Collection<T> col, @Name("tarType") Class<? extends T[]> tarType) {
        Object[] arr = new Object[0];
        if (CheckUtil.isEmptyCollection(col)) {
            return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
        }
        arr = new Object[col.size()];
        Iterator it = col.iterator();
        int ix = 0;
        while (it.hasNext()) {
            arr[ix] = it.next();
            ix++;
        }
        return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
    }

    public static <T> T[] copyArray(@Name("index")int index, @Name("len")int len, @Name("tarType")Class<? extends T[]> tarType,@Name("srcArr") T ... srcArr) {
        List<T> list = Collections.collect(new ArrayList<>(), null, index, len, srcArr);
        return collect(list, tarType);
    }

    public static <T,E> T[] copy(@Name("index")int index,@Name("len")int len,@Name("tarType")Class<? extends T[]> tarType,@Name("srcArr")E ... srcArr){
        Object[] ret=copyArray(index,len,Object[].class,srcArr);
        return (T[]) java.util.Arrays.copyOf(ret, ret.length);
    }


    public static <T> T[] collectKeys(@Name("map") Map<T, ?> map, @Name("tarType") Class<? extends T[]> tarType) {
        Object[] arr = new Object[0];
        if (CheckUtil.isEmptyMap(map)) {
            return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
        }
        arr = new Object[map.size()];
        Iterator it = map.keySet().iterator();
        int ix = 0;
        while (it.hasNext()) {
            arr[ix] = it.next();
            ix++;
        }
        return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
    }

    public static <T, E> T[] collectValues(@Name("map") Map<?, T> map, @Name("tarType") Class<? extends E[]> tarType) {
        Object[] arr = new Object[0];
        if (CheckUtil.isEmptyMap(map)) {
            return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
        }
        arr = new Object[map.size()];
        Iterator it = map.keySet().iterator();
        int ix = 0;
        while (it.hasNext()) {
            arr[ix] = map.get(it.next());
            ix++;
        }
        return (T[]) java.util.Arrays.copyOf(arr, arr.length, tarType);
    }

    public static <T> T[] merge(@Name("ites") Iterable<T>... ites) {
        List<T> list = Collections.merge(new LinkedList<T>(), ites);
        int size = list.size();
        Object[] ret = new Object[size];
        int i = 0;
        for (T item : list) {
            ret[i++] = item;
        }
        return (T[]) java.util.Arrays.copyOf(ret, size);
    }

    public static <T> T[] merge(@Name("enums") Enumeration<T>... enums) {
        List<T> list = Collections.merge(new LinkedList<T>(), enums);
        int size = list.size();
        Object[] ret = new Object[size];
        int i = 0;
        for (T item : list) {
            ret[i++] = item;
        }
        return (T[]) java.util.Arrays.copyOf(ret, size);
    }

    public static <T> T[] merge(@Name("arr1") T[] arr1, @Name("arr2") T... arr2) {
        int size = arr1.length + arr2.length;
        Object[] ret = new Object[size];
        int i = 0;
        for (T item : arr1) {
            ret[i++] = item;
        }
        for (T item : arr2) {
            ret[i++] = item;
        }
        return (T[]) java.util.Arrays.copyOf(ret, size);
    }

    public static <T> int compare(T[] arr1, T[] arr2, Comparator<T> comparator) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            T v1 = arr1[i];
            T v2 = arr2[i];
            int rs = comparator.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }

    public static int compare(byte[] arr1, byte[] arr2) {
        if (arr1 == arr2) {
            return 0;
        }
        if (arr1 == null) {
            return -1;
        }
        if (arr2 == null) {
            return 1;
        }
        int i = 0;
        while (i < arr1.length && i < arr2.length) {
            byte v1 = arr1[i];
            byte v2 = arr2[i];
            int rs = Byte.compare(v1, v2);
            if (rs != 0) {
                return rs;
            }
            i++;
        }

        if (i < arr1.length) {
            return 1;
        }
        if (i < arr2.length) {
            return -1;
        }
        return 0;
    }
}
