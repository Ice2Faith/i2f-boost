package i2f.core.dict.type.impl;


import i2f.core.dict.type.ITypeConverter;
import i2f.core.dict.type.TypeUtil;

/**
 * @author Ice2Faith
 * @date 2023/2/21 14:06
 * @desc
 */
public class Object2StringConverter implements ITypeConverter {

    @Override
    public boolean support(Object obj, Class<?> tarType) {
        return TypeUtil.instanceOf(tarType, String.class);
    }

    @Override
    public Object convert(Object obj, Class<?> tarType) {
        return obj == null ? null : String.valueOf(obj);
    }
}
