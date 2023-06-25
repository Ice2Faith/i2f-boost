package i2f.core.container.iterator.impl;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * 对多维数组进行遍历
 * 泛型是针对迭代的返回元素的类型转换
 * 核心是通过 flatArrayAsList 方法，将数组展开为list，基于list进行线性遍历
 * 支持固定数组
 * Integer[][][] arr=new Integer[][][]{
 * {{1},{2,3},{4,5,6}},
 * {{7,8},{9},{10,11}},
 * {{12,13,14},{15,16},{17},{18,19,20}},
 * {{21,22},{23}},
 * };
 * 可变数组
 * Object[] dynamicArr=new Object[]{
 * new Integer[][]{
 * {1,2},{3},{4,5,6}
 * },
 * new Integer[]{
 * 7,8,9,10,11
 * },
 * new Integer[][][]{
 * {{12,13,14},{15,16},{17},{18,19,20}},
 * {{21,22},{23}},
 * }
 * };
 *
 * @param <T>
 */
public class MultiDimensionArrayIterator<T> implements Iterator<T> {
    private Object arr;
    private List<ArrayRouter> list = new LinkedList<>();
    private Iterator<ArrayRouter> listIter;

    public static class ArrayRouter {
        public Object elem;
        public List<Integer> trace = new LinkedList<>();

        @Override
        public String toString() {
            return "ArrayRouter{" +
                    "elem=" + elem +
                    ", trace=" + trace +
                    '}';
        }
    }

    public MultiDimensionArrayIterator(Object arr) {
        this.arr = arr;
    }

    public static void flatArrayAsList(Object arr, List<ArrayRouter> list, LinkedList<Integer> trace) {
        if (isArray(arr)) {
            int len = Array.getLength(arr);
            for (int i = 0; i < len; i++) {
                LinkedList<Integer> nextTrace = new LinkedList<>();
                nextTrace.addAll(trace);
                nextTrace.add(i);
                flatArrayAsList(Array.get(arr, i), list, nextTrace);
            }
        } else {
            ArrayRouter item = new ArrayRouter();
            item.elem = arr;
            item.trace.addAll(trace);
            list.add(item);
        }
    }

    public static boolean isArray(Object obj) {
        return obj != null && obj.getClass().isArray();
    }

    @Override
    public boolean hasNext() {
        if (listIter == null) {
            flatArrayAsList(arr, list, new LinkedList<>());
            this.listIter = list.iterator();
        }
        return this.listIter.hasNext();
    }

    @Override
    public T next() {
        ArrayRouter obj = this.listIter.next();
        return (T) obj.elem;
    }

    public ArrayRouter nextWithRouter() {
        return this.listIter.next();
    }
}
