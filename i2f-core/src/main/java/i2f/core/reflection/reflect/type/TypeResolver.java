package i2f.core.reflection.reflect.type;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author ltb
 * @date 2022/4/14 10:20
 * @desc
 */
public class TypeResolver {
    public static final Class[] BASE_TYPES_ARRAY={
            int.class,long.class,short.class,
            byte.class,char.class,
            float.class,double.class,
            boolean.class,
            String.class,
            Integer.class,Long.class,Short.class,
            Byte.class,Character.class,
            Float.class,Double.class,
            Boolean.class,
            BigInteger.class, BigDecimal.class
    };

    public static final Class[] BIG_DECIMAL_COMPATIBLE_TYPES_ARRAY={
            int.class,long.class,short.class,
            byte.class,
            float.class,double.class,
            Integer.class,Long.class,Short.class,
            Byte.class,
            Float.class,Double.class,
            BigInteger.class, BigDecimal.class
    };

    public static final Class[] BIG_INTEGER_COMPATIBLE_TYPES_ARRAY={
            int.class,long.class,short.class,
            byte.class,
            Integer.class,Long.class,Short.class,
            Byte.class,
            BigInteger.class
    };

    public static boolean isBaseType(Class ckType){
        return isInTypes(ckType,BASE_TYPES_ARRAY);
    }

    public static boolean isBigIntegerCompatibleType(Class ckType){
        return isInTypes(ckType,BIG_INTEGER_COMPATIBLE_TYPES_ARRAY);
    }

    public static boolean isBigDecimalCompatibleType(Class ckType){
        return isInTypes(ckType,BIG_DECIMAL_COMPATIBLE_TYPES_ARRAY);
    }

    public static boolean isInTypes(Class ckType,Class ... tarTypes){
        if(ckType==null){
            return false;
        }
        if(tarTypes==null || tarTypes.length==0){
            return false;
        }
        for(Class item : tarTypes){
            if(ckType.equals(item)){
                return true;
            }
            //该方法用于判定，父类target是否派生出了子类item
            if(item.isAssignableFrom(ckType)){
                return true;
            }
        }
        return false;
    }
}
