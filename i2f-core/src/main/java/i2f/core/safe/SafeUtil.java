package i2f.core.safe;

import i2f.core.check.CheckUtil;

/**
 * @author ltb
 * @date 2022/3/26 14:49
 * @desc
 */
public class SafeUtil {
    public static Object ifNull(Object obj,Object defVal){
        if(CheckUtil.isNull(obj)){
            return defVal;
        }
        return obj;
    }
    public static<T> T ifNullChk(T obj,T defVal){
        if(CheckUtil.isNull(obj)){
            return defVal;
        }
        return obj;
    }
    public static String ifEmptyStr(String str,String defVal){
        if(CheckUtil.isEmptyStr(str)){
            return defVal;
        }
        return str;
    }
    public static Object ifTrue(Object obj,boolean condition,Object defVal){
        if(condition){
            return defVal;
        }
        return obj;
    }
    public static Object ifFalse(Object obj,boolean condition,Object defVal){
        if(!condition){
            return defVal;
        }
        return obj;
    }
    public static<T> T ifTrueChk(T obj,boolean condition,T defVal){
        if(condition){
            return defVal;
        }
        return obj;
    }
    public static<T> T ifFalseChk(T obj,boolean condition,T defVal){
        if(!condition){
            return defVal;
        }
        return obj;
    }

}
