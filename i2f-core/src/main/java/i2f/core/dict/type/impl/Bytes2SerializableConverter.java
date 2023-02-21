package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Bytes2SerializableConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        if (!TypeUtil.instanceOfAny(tarType, Serializable.class)) {
            return false;
        }
        if (obj == null) {
            return true;
        }
        return TypeUtil.instanceOfAny(obj.getClass(), byte[].class);
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        if (obj == null) {
            return null;
        }
        byte[] data = (byte[]) obj;
        if (data.length == 0) {
            return null;
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            Object val = ois.readObject();
            ois.close();
            return val;
        } catch (Exception e) {

        }

        return null;
    }
}
