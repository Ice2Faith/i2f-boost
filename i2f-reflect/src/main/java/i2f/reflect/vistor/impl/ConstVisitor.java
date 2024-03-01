package i2f.reflect.vistor.impl;

import i2f.reflect.vistor.Visitor;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:01
 * @desc
 */
public class ConstVisitor implements Visitor {
    private Object val;

    public ConstVisitor(Object val) {
        this.val = val;
    }

    @Override
    public Object get() {
        return this.val;
    }

    @Override
    public void set(Object value) {
        throw new IllegalStateException("this visitor is readonly, not support set value");
    }
}
