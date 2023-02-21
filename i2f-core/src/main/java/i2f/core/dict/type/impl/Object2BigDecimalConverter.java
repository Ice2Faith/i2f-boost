package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.math.BigDecimal;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2BigDecimalConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, BigDecimal.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOf(obj.getClass(), BigDecimal.class);
        if (ok) {
            return true;
        }
        try {
            new BigDecimal(String.valueOf(obj));
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
        boolean ok = TypeUtil.instanceOf(obj.getClass(), BigDecimal.class);
        if (ok) {
            return (BigDecimal) obj;
        }
        try {
            return new BigDecimal(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
