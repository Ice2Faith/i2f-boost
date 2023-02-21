package i2f.core.dict.type;


import i2f.core.dict.type.impl.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/2/21 15:57
 * @desc
 */
public class ConvertUtil {

    private static List<ITypeConverter> converters = Collections.unmodifiableList(new ArrayList<>());

    static {
        List<ITypeConverter> ret = new ArrayList<>();
        ret.add(new Object2ObjectConverter());
        ret.add(new Object2StringConverter());

        ret.add(new Object2IntegerConverter());
        ret.add(new Object2LongConverter());
        ret.add(new Object2ShortConverter());

        ret.add(new Object2DoubleConverter());
        ret.add(new Object2FloatConverter());

        ret.add(new Object2CharacterConverter());
        ret.add(new Object2ByteConverter());

        ret.add(new Object2BooleanConverter());

        ret.add(new Object2BigIntegerConverter());
        ret.add(new Object2BigDecimalConverter());

        ret.add(new Date2TimestampConverter());
        ret.add(new Timestamp2DateConverter());

        ret.add(new Date2LocalDateConverter());
        ret.add(new LocalDate2DateConverter());

        ret.add(new Date2LocalDateTimeConverter());
        ret.add(new LocalDateTime2DateConverter());

        ret.add(new String2DateConverter());

        ret.add(new Serializable2BytesConverter());
        ret.add(new Bytes2SerializableConverter());

        ret.add(new String2BytesConverter());
        ret.add(new Bytes2StringConverter());

        converters = Collections.unmodifiableList(ret);
    }


    public static List<ITypeConverter> defaultConverters() {
        return converters;
    }

    public static boolean canConvert(Object obj, Class<?> tarType) {
        return canConvert(obj, tarType, defaultConverters());
    }

    public static boolean canConvert(Object obj, Class<?> tarType, List<ITypeConverter> converters) {
        for (ITypeConverter converter : converters) {
            if (converter.support(obj, tarType)) {
                return true;
            }
        }
        return false;
    }

    public static Object tryConvert(Object obj, Class<?> tarType) {
        return tryConvert(obj, tarType, null, defaultConverters());
    }

    public static Object tryConvert(Object obj, Class<?> tarType, Object elseVal) {
        return tryConvert(obj, tarType, elseVal, defaultConverters());
    }

    public static Object tryConvert(Object obj, Class<?> tarType, Object elseVal, List<ITypeConverter> converters) {
        for (ITypeConverter converter : converters) {
            if (converter.support(obj, tarType)) {
                return converter.convert(obj, tarType);
            }
        }
        return elseVal;
    }

}
