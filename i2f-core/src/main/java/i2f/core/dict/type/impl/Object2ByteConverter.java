package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2ByteConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Byte.class, byte.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Byte.class, byte.class);
        if (ok) {
            return true;
        }
        try {
            Byte.parseByte(String.valueOf(obj));
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
        boolean ok = TypeUtil.instanceOfAny(obj.getClass(), Byte.class, byte.class);
        if (ok) {
            return (Byte) obj;
        }
        try {
            return Byte.parseByte(String.valueOf(obj));
        } catch (Exception e) {

        }
        return null;
    }
}
