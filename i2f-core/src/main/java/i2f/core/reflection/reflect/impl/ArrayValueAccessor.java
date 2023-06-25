package i2f.core.reflection.reflect.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.interfaces.ValueAccessor;

import java.lang.reflect.Array;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
@Author("i2f")
public class ArrayValueAccessor implements ValueAccessor {
    public Object arr;
    public int idx;
    public ArrayValueAccessor(Object arr,int idx) {
        this.arr=arr;
        this.idx=idx;
    }

    @Override
    public Object get() {
        return Array.get(arr,idx);
    }

    @Override
    public void set(Object obj) {
        Array.set(arr,idx,obj);
    }

    public Object getArray(){
        return arr;
    }
    public int getIndex(){
        return idx;
    }
}
