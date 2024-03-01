package i2f.reflect.vistor.impl;

import i2f.reflect.vistor.Visitor;

import java.util.Map;

/**
 * @author Ice2Faith
 * @date 2024/3/1 19:05
 * @desc
 */
public class MapVisitor implements Visitor {
    private Map map;
    private Object key;

    public MapVisitor(Map map, Object key) {
        this.map = map;
        this.key = key;
    }

    @Override
    public Object get() {
        return map.get(key);
    }

    @Override
    public void set(Object value) {
        map.put(key,value);
    }

    @Override
    public Object parent() {
        return this.map;
    }
}
