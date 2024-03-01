package i2f.reflect.vistor.impl;

import i2f.reflect.vistor.Visitor;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:01
 * @desc
 */
public class ReadonlyVisitor implements Visitor {
    private Object val;
    private Object parent;

    public ReadonlyVisitor(Object val,Object parent) {
        this.val = val;
        this.parent=parent;
    }

    @Override
    public Object get() {
        return this.val;
    }

    @Override
    public void set(Object value) {
        throw new IllegalStateException("this visitor is readonly, not support set value");
    }

    @Override
    public Object parent() {
        return this.parent;
    }
}
