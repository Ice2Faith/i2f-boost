package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Serializable2BytesConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, byte[].class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        return TypeUtil.instanceOfAny(obj.getClass(), Serializable.class);
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        if (obj == null) {
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
        } catch (Exception e) {

        }

        return bos.toByteArray();
    }
}
