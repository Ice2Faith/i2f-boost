package i2f.core.type.number;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author ltb
 * @date 2022/5/19 19:40
 * @desc
 */
public class Numbers {
    protected static <T> T cvt(T num1,BigDecimal rs){
        if(num1 instanceof BigDecimal){
            return (T)rs;
        }
        if(num1 instanceof Integer){
            return (T)(Integer)rs.intValue();
        }
        if(num1 instanceof Long){
            return (T)(Long)rs.longValue();
        }
        if(num1 instanceof Short){
            return (T)(Short)rs.shortValue();
        }
        if(num1 instanceof Float){
            return (T)(Float)rs.floatValue();
        }
        if(num1 instanceof Double){
            return (T)(Double)rs.doubleValue();
        }
        if(num1 instanceof BigInteger){
            return (T)(BigInteger)rs.toBigInteger();
        }
        throw new UnsupportedOperationException("un-support number type");
    }
    public static <T> T add(T num1,T num2){
        if(num1==null || num2==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal val2=new BigDecimal(String.valueOf(num2));
        BigDecimal rs=val1.add(val2);
        return cvt(num1,rs);
    }
    public static <T> T sub(T num1,T num2){
        if(num1==null || num2==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal val2=new BigDecimal(String.valueOf(num2));
        BigDecimal rs=val1.subtract(val2);
        return cvt(num1,rs);
    }
    public static <T> T mul(T num1,T num2){
        if(num1==null || num2==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal val2=new BigDecimal(String.valueOf(num2));
        BigDecimal rs=val1.multiply(val2);
        return cvt(num1,rs);
    }
    public static <T> T div(T num1,T num2){
        if(num1==null || num2==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal val2=new BigDecimal(String.valueOf(num2));
        BigDecimal rs=val1.divide(val2);
        return cvt(num1,rs);
    }
    public static <T> T abs(T num1){
        if(num1==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal rs=val1.abs();
        return cvt(num1,rs);
    }
    public static <T> int cmp(T num1,T num2){
        if(num1==null || num2==null){
            throw new ArithmeticException("operation number not allow is null");
        }
        BigDecimal val1=new BigDecimal(String.valueOf(num1));
        BigDecimal val2=new BigDecimal(String.valueOf(num2));
        return val1.compareTo(val2);
    }
    public static <T> boolean eq(T num1,T num2){
        return cmp(num1,num2)==0;
    }
    public static <T> boolean neq(T num1,T num2){
        return cmp(num1,num2)!=0;
    }
    public static <T> boolean gt(T num1,T num2){
        return cmp(num1,num2)>0;
    }
    public static <T> boolean gte(T num1,T num2){
        return cmp(num1,num2)>=0;
    }
    public static <T> boolean lt(T num1,T num2){
        return cmp(num1,num2)<0;
    }
    public static <T> boolean lte(T num1,T num2){
        return cmp(num1,num2)<=0;
    }
}
