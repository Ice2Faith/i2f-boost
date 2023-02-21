package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.math.BigInteger;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2BigIntegerConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, BigInteger.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOf(obj.getClass(), BigInteger.class);
        if (ok) {
            return true;
        }
        try {
            new BigInteger(String.valueOf(obj));
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
        boolean ok = TypeUtil.instanceOf(obj.getClass(), BigInteger.class);
        if (ok) {
            return (BigInteger) obj;
        }
        try {
            return new BigInteger(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
