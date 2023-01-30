package i2f.springboot.sensible.handler;


import i2f.springboot.sensible.ISensibleHandler;
import i2f.springboot.sensible.Sensible;
import i2f.springboot.sensible.SensibleType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class AbsDictSensibleHandler implements ISensibleHandler {
    @Override
    public Set<String> accept() {
        return new HashSet<>(Arrays.asList(
                SensibleType.DICT
        ));
    }

    @Override
    public Set<Class<?>> type() {
        return new HashSet<>(Arrays.asList(Object.class));
    }

    @Override
    public Object handle(Object obj, Sensible ann) {
        if (obj == null) {
            return obj;
        }
        String dictType = ann.param();

        return decodeDict(dictType, obj);
    }

    protected abstract Object decodeDict(String dictType, Object dictValue);
}
