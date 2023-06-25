package i2f.core.reflection.reflect.impl;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.reflect.interfaces.ValueAccessor;

import java.util.Map;

/**
 * @author ltb
 * @date 2022/3/14 9:28
 * @desc
 */
@Author("i2f")
public class MapValueAccessor implements ValueAccessor {
    public Map map;
    public Object key;
    public MapValueAccessor(Map map, Object key) {
        this.map = map;
        this.key=key;
    }

    @Override
    public Object get() {
        return map.get(key);
    }

    @Override
    public void set(Object obj) {
        map.put(key,obj);
    }

    public Map getMap(){
        return map;
    }

    public Object getKey(){
        return key;
    }
}
