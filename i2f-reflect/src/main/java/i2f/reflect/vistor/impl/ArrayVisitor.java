package i2f.reflect.vistor.impl;

import i2f.reflect.ReflectResolver;
import i2f.reflect.vistor.Visitor;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:05
 * @desc
 */
public class ArrayVisitor implements Visitor {
    private Object arr;
    private int index;

    public ArrayVisitor(Object arr, int index) {
        this.arr = arr;
        this.index = index;
    }

    @Override
    public Object get() {
        return ReflectResolver.arrayGet(arr,index);
    }

    @Override
    public void set(Object value) {
        ReflectResolver.arraySet(arr,index,value);
    }

    @Override
    public Object parent() {
        return this.arr;
    }
}
