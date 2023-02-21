package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Timestamp2DateConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Date.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        return TypeUtil.instanceOfAny(obj.getClass(), Date.class, Timestamp.class);
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        if (obj == null) {
            return null;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Date.class);
        if (ok) {
            return (Date) obj;
        }
        return new Date(((Timestamp) obj).getTime());
    }
}
