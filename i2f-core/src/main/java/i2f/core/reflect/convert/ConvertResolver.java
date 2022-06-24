package i2f.core.reflect.convert;


import i2f.core.annotations.remark.Author;
import i2f.core.check.CheckUtil;
import i2f.core.reflect.type.TypeResolver;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.*;

/**
 * @author ltb
 * @date 2022/3/14 11:24
 * @desc
 */
@Author("i2f")
public class ConvertResolver {
    public static boolean canConvert2Number(Object srcObj) {
        return (srcObj instanceof String && CheckUtil.isFloatNumber((String) srcObj))
                || isValueConvertible(srcObj, BigInteger.class)
                || isValueConvertible(srcObj, BigDecimal.class);
    }

    public static BigDecimal convert2Number(Object srcObj) {
        if (srcObj instanceof String && CheckUtil.isFloatNumber((String) srcObj)) {
            return new BigDecimal((String) srcObj);
        }
        if (isValueConvertible(srcObj,BigInteger.class)) {
            BigInteger iv = (BigInteger) tryConvertible(srcObj, BigInteger.class);
            BigDecimal dv = new BigDecimal(iv.toString());
            return dv;
        }
        return (BigDecimal) tryConvertible(srcObj, BigDecimal.class);
    }
    public static boolean isInTypes(Class ckType,Class ... tarTypes){
        return TypeResolver.isInTypes(ckType, tarTypes);
    }

    public static boolean isValueConvertible(Object val,Class dstType){
        if(val==null){
            return true;
        }
        Class clazz=val.getClass();
        if(val instanceof String){
            String sval=(String)val;
            if(isInteger(dstType) && sval.matches("^\\d+$")){
                return true;
            }
            if(isFloat(dstType) && sval.matches("^\\d+(\\.\\d+)?$")){
                return true;
            }
            if(isBoolean(dstType) && sval.toLowerCase().matches("^true|false|yes|no|1|0$")){
                return true;
            }
            if(isCharacter(dstType) && sval.length()==1){
                return true;
            }
        }
        return isConvertible(clazz,dstType);
    }

    public static boolean isCharacter(Class clazz){
        return isInTypes(clazz,char.class,Character.class);
    }

    public static boolean isInteger(Class clazz){
        return TypeResolver.isBigIntegerCompatibleType(clazz);
    }

    public static boolean isFloat(Class clazz){
        return TypeResolver.isBigDecimalCompatibleType(clazz);
    }

    public static boolean isBoolean(Class clazz){
        return isInTypes(clazz,boolean.class,Boolean.class);
    }

    public static boolean isConvertible(Class srcType,Class dstType){
        if(srcType==null){
            return false;
        }
        if(dstType==null){
            return false;
        }
        if(Void.class.equals(dstType)){
            return false;
        }
        // Object 类型都可以转换
        if(Object.class.equals(dstType)){
            return true;
        }
        // String 类型都可以转换
        if(String.class.equals(dstType)){
            return true;
        }
        // 同类型或者派生关系都可以转换
        if(isInTypes(srcType,dstType)){
            return true;
        }
        // 整形之间都可以转换
        if(isInteger(srcType)
                &&
                isInteger(dstType)
        ){
            return true;
        }

        // 浮点数之间可转换，整形可转换为浮点类型
        if(isInTypes(srcType,float.class,Float.class,
                double.class,Double.class,
                BigDecimal.class,
                int.class,Integer.class,
                short.class,Short.class,
                long.class,Long.class,
                byte.class,Byte.class,
                BigInteger.class)
                &&
                isFloat(dstType)
        ){
            return true;
        }

        // 字符型之间可以转换
        if(isInTypes(srcType,char.class,Character.class)
            &&
                isInTypes(dstType,char.class,Character.class)
        ){
            return true;
        }

        // 时间类型之间可以转换
        if(isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class, java.util.Calendar.class,
                java.time.LocalDate.class,java.time.LocalDateTime.class)
         &&
                isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class, java.util.Calendar.class,
                        java.time.LocalDate.class,java.time.LocalDateTime.class)
        ){
            return true;
        }

        return false;
    }

    public static Object tryConvertible(Object val,Class dstType){
        if(val==null){
            return val;
        }
        if(Object.class.equals(dstType)){
            return val;
        }
        if(String.class.equals(dstType)){
            return String.valueOf(val);
        }
        Class srcType=val.getClass();
        if(isInTypes(srcType,dstType)){
            return val;
        }
        if(val instanceof String){
            String sval=(String)val;
            if(isInteger(dstType) && sval.matches("^\\d+$")){
                val=new BigInteger(sval);
                srcType=val.getClass();
            }
            if(isFloat(dstType) && sval.matches("^\\d+(\\.\\d+)?$")){
                val=new BigDecimal(sval);
                srcType=val.getClass();
            }
            if(isBoolean(dstType) && sval.toLowerCase().matches("^true|false|yes|no|1|0$")){
                if("true".equals(sval.toLowerCase())){
                    return true;
                }else if("false".equals(sval.toLowerCase())){
                    return false;
                }else if("yes".equals(sval.toLowerCase())){
                    return true;
                }else if("no".equals(sval.toLowerCase())){
                    return false;
                }else if("1".equals(sval.toLowerCase())){
                    return true;
                }else if("0".equals(sval.toLowerCase())){
                    return false;
                }
            }
            if(isCharacter(dstType) && sval.length()==1){
                return sval.charAt(0);
            }
        }
        if(isInteger(srcType)
                &&
                isInteger(dstType)
        ){
            BigInteger ival=new BigInteger(String.valueOf(val));
            if(isInTypes(dstType,BigInteger.class)){
                return ival;
            }
            else if(isInTypes(dstType,int.class,Integer.class)){
                return ival.intValue();
            }else if(isInTypes(dstType,short.class,Short.class)){
                return ival.shortValue();
            }else if(isInTypes(dstType,long.class,Long.class)){
                return ival.longValue();
            }else if(isInTypes(dstType,byte.class,Byte.class)){
                return ival.byteValue();
            }
        }

        if(isInTypes(srcType,float.class,Float.class,
                double.class,Double.class,
                BigDecimal.class,
                int.class,Integer.class,
                short.class,Short.class,
                long.class,Long.class,
                byte.class,Byte.class,
                BigInteger.class)
                &&
                isFloat(dstType)
        ){
            BigDecimal dval=new BigDecimal(String.valueOf(val));
            if(isInTypes(dstType,BigDecimal.class)){
                return dval;
            }
            else if(isInTypes(dstType,float.class,Float.class)){
                return dval.floatValue();
            }else if(isInTypes(dstType,double.class,Double.class)){
                return dval.doubleValue();
            }
        }

        if(isInTypes(srcType,char.class,Character.class)
                &&
                isInTypes(dstType,char.class,Character.class)
        ){
            Character cval=(Character)val;
            return cval;
        }

        if(isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class, java.util.Calendar.class,
                java.time.LocalDate.class,java.time.LocalDateTime.class)
                &&
                isInTypes(srcType,java.util.Date.class,java.sql.Timestamp.class,java.sql.Date.class, java.util.Calendar.class,
                        java.time.LocalDate.class,java.time.LocalDateTime.class)
        ){
            Object pval=val;

            if(val instanceof LocalDate){
                ZoneId zone=ZoneId.systemDefault();
                Instant instant = ((LocalDate)val).atStartOfDay().atZone(zone).toInstant();
                pval= Date.from(instant);
            }else if(val instanceof LocalDateTime){
                ZoneId zone=ZoneId.systemDefault();
                Instant instant = ((LocalDateTime)val).atZone(zone).toInstant();
                pval= Date.from(instant);
            }else if(val instanceof java.util.Calendar){
                pval=((java.util.Calendar)val).getTime();
            }

            long tval=((java.util.Date)pval).getTime();
            if(isInTypes(dstType,java.sql.Timestamp.class)){
                return new java.sql.Timestamp(tval);
            }else if(isInTypes(dstType,java.sql.Date.class)){
                return new java.sql.Date(tval);
            }else if(isInTypes(dstType,java.util.Date.class)){
                return new java.util.Date(tval);
            }else if(isInTypes(dstType,java.time.LocalDate.class)){
                Instant instant = new java.util.Date(tval).toInstant();
                ZoneId zone=ZoneId.systemDefault();
                return LocalDateTime.ofInstant(instant, zone).toLocalDate();
            }else if(isInTypes(dstType,java.time.LocalDateTime.class)){
                Instant instant = new java.util.Date(tval).toInstant();
                ZoneId zone=ZoneId.systemDefault();
                return LocalDateTime.ofInstant(instant, zone);
            }else if(isInTypes(dstType,java.util.Calendar.class)){
                java.util.Calendar ret=java.util.Calendar.getInstance();
                ret.setTime(new java.util.Date(tval));
                return ret;
            }
        }

        return val;
    }
}
