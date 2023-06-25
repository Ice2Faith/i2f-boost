package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;
import i2f.core.type.date.Dates;

import java.time.LocalDate;
import java.util.Date;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Date2LocalDateConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, LocalDate.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        return TypeUtil.instanceOfAny(obj.getClass(), Date.class, LocalDate.class);
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        if (obj == null) {
            return null;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), LocalDate.class);
        if (ok) {
            return (LocalDate) obj;
        }
        return Dates.date2LocalDate((Date) obj);
    }
}
