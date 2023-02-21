package i2f.core.dict.type.impl;

import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2DoubleConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Double.class, double.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Double.class, double.class);
        if (ok) {
            return true;
        }
        try {
            Double.parseDouble(String.valueOf(obj));
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
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Double.class, double.class);
        if (ok) {
            return (Double) obj;
        }
        try {
            return Double.parseDouble(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
