package i2f.core.convert.impl;

import i2f.core.convert.IKeyProvider;
import i2f.core.reflection.reflect.ValueResolver;

/**
 * @author ltb
 * @date 2022/3/27 18:33
 * @desc
 */
public class ObjectKeyProvider implements IKeyProvider<Object,String> {
    public String key;
    public ObjectKeyProvider(String key){
        this.key=key;
    }
    @Override
    public Object getKey(String obj) {
        return ValueResolver.get(obj,key);
    }
}
