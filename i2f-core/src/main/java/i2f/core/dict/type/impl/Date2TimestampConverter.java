package i2f.core.dict.type.impl;


import i2f.core.date.Dates;
import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Date2TimestampConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Timestamp.class)) {
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
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Timestamp.class);
        if (ok) {
            return (Timestamp) obj;
        }
        return Dates.date2Timestamp((Date) obj);
    }
}
