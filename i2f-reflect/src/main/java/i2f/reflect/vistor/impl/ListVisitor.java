package i2f.reflect.vistor.impl;

import i2f.reflect.vistor.Visitor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:05
 * @desc
 */
public class ListVisitor implements Visitor {
    private List list;
    private int index;

    public ListVisitor(List list, int index) {
        this.list = list;
        this.index = index;
    }

    @Override
    public Object get() {
        return list.get(index);
    }

    @Override
    public void set(Object value) {
        list.set(index,value);
    }

    @Override
    public Object parent() {
        return this.list;
    }
}
