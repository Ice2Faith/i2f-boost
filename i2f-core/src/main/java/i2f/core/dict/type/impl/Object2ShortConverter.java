package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2ShortConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Short.class, short.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Short.class, short.class);
        if (ok) {
            return true;
        }
        try {
            Short.parseShort(String.valueOf(obj));
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {

        if (obj == null) {
            return null;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Short.class, short.class);
        if (ok) {
            return (Short) obj;
        }
        try {
            return Short.parseShort(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
