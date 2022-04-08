package i2f.core.convert;

import i2f.core.convert.impl.ObjectKeyProvider;
import i2f.core.convert.impl.ObjectValueProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ltb
 * @date 2022/3/26 14:59
 * @desc
 */
public class ConvertUtil {
    public static String toStr(Object obj){
        return toStr(obj,true);
    }
    public static String toStr(Object obj,boolean keepNull){
        if(obj==null && !keepNull){
            return "";
        }
        return String.valueOf(obj);
    }
    public static int toInt(Object obj,int defVal){
        try{
            return Integer.parseInt(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static long toLong(Object obj,long defVal){
        try{
            return Long.parseLong(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static short toShort(Object obj,short defVal){
        try{
            return Short.parseShort(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static boolean toBoolean(Object obj,boolean defVal){
        try{
            return Boolean.parseBoolean(toStr(obj,false).toLowerCase());
        }catch(Exception e){
            return defVal;
        }
    }
    public static float toFloat(Object obj,float defVal){
        try{
            return Float.parseFloat(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static double toDouble(Object obj,double defVal){
        try{
            return Double.parseDouble(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static Date toDate(Object obj, String patten, Date defVal){
        try{
            return new SimpleDateFormat(patten).parse(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static BigInteger toBigInteger(Object obj,BigInteger defVal){
        try{
            return new BigInteger(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }
    public static BigDecimal toBigDecimal(Object obj,BigDecimal defVal){
        try{
            return new BigDecimal(toStr(obj,false));
        }catch(Exception e){
            return defVal;
        }
    }

}
