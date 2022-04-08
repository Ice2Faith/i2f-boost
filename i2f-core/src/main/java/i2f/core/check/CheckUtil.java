package i2f.core.check;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Author("i2f")
@Remark("provider value check")
public class CheckUtil {
    @Remark("a <= {ch} <= z")
    public static boolean isChLower(@Name("ch") char ch) {
        return ch >= 'a' && ch <= 'z';
    }
    @Remark("A <= {ch} <= Z")
    public static boolean isChUpper(@Name("ch")char ch){
        return ch>='A' && ch<='Z';
    }
    @Remark("{ch} in a-zA-Z")
    public static boolean isCh(@Name("ch")char ch){
        return isChLower(ch) || isChUpper(ch);
    }
    @Remark("{ch} in 0-9")
    public static boolean isChNum(@Name("ch")char ch){
        return ch>='0' && ch<='9';
    }
    @Remark({"{ch} in 0-9A-Za-Z on digital {base}","base in [2,36]"})
    public static boolean isChNum(@Name("ch")char ch,@Name("base") int base){
        if(base<2 || base>(10+26)){
            return false;
        }
        if(base<=10){
            if(ch>='0' && ch<=('0'+base-1)){
                return true;
            }
        }else{
            if(isChNum(ch)){
                return true;
            }
            int off=base-10;
            if(ch>='A' && ch<=('A'+off-1)){
                return true;
            }
            if(ch>='a' && ch<=('a'+off-1)){
                return true;
            }
        }
        return false;
    }
    @Remark("{tar} in {vals}")
    public static boolean isIn(@Name("tar")@Nullable Object tar, @Name("vals")@Nullable Object ... vals){
        if(isEmptyArray(vals)){
            return false;
        }
        for(Object item : vals){
            if(isNull(tar)){
                if(isNull(item)){
                    return true;
                }
            }else{
                if(tar.equals(item)){
                    return true;
                }
            }
        }
        return false;
    }
    @Remark("{tar} not in {vals}")
    public static boolean notIn(@Name("tar")@Nullable Object tar, @Name("vals")@Nullable Object ... vals){
        return !isIn(tar,vals);
    }
    @Remark("is exists true in {bools}")
    public static boolean isExTrue(@Name("bools") boolean  ... bools){
        if(isEmptyArray(bools)){
            return false;
        }
        for(boolean item : bools){
            if(item){
                return true;
            }
        }
        return false;
    }
    @Remark("is exists false in {bools}")
    public static boolean isExFalse(@Name("bools")boolean  ... bools){
        if(isEmptyArray(bools)){
            return false;
        }
        for(boolean item : bools){
            if(!item){
                return true;
            }
        }
        return false;
    }
    @Remark("is exists null in {objs}")
    public static boolean isExNull(@Name("objs") @Nullable Object ... objs){
        if(isEmptyArray(objs)){
            return false;
        }
        for(Object item : objs){
            if(isNull(item)){
                return true;
            }
        }
        return false;
    }

    @Remark("{obj} is null")
    public static boolean isNull(@Name("obj")@Nullable Object obj){
        return obj==null;
    }

    @Remark("decision obj whether is Array type")
    public static boolean isArray(@Name("arr") Object arr){
        if(arr==null){
            return false;
        }
        return arr.getClass().isArray();
    }

    public static boolean notArray(@Name("arr") Object arr){
        return !isArray(arr);
    }
    public static boolean isExEmptyStr(@Name("strs")@Nullable String ... strs){
        return isExEmptyStr(false,strs);
    }
    @Remark("is exists empty string in {strs},and you can do trim then do compare")
    public static boolean isExEmptyStr(@Name("needTrimed") boolean needTrimed,@Name("strs")@Nullable String ... strs){
        if(isEmptyArray(strs)){
            return false;
        }
        for(String item : strs){
            if(isEmptyStr(item,needTrimed)){
                return true;
            }
        }
        return false;
    }
    /**
     * 检查是否是null或者是否是空串，根据需要进行trim之后比较是否空串
     * @param str 串
     * @param needTrimed 是否需要trim判断
     * @return
     */
    @Remark("{str} is empty string or null,and you can do trim then do compare")
    public static boolean isEmptyStr(@Name("str")@Nullable String str,@Name("needTrimed") boolean needTrimed){
        if(isNull(str)) {
            return true;
        }
        if(needTrimed){
            return "".equals(str.trim());
        }
        return "".equals(str);
    }
    @Remark("{str} is empty or null")
    public static boolean isEmptyStr(@Name("str")@Nullable String str){
        return isEmptyStr(str,false);
    }

    @Remark("{arrs} is exists empty array or null")
    public static boolean isExEmptyArray(@Name("arrs")@Nullable Object ... arrs){
        if(isEmptyArray(arrs)){
            return false;
        }
        for(Object item : arrs){
            if(isEmptyArray(item)){
                return true;
            }
        }
        return false;
    }

    @Remark("{arr} is empty array or null")
    public static<T> boolean isEmptyArray(@Name("arr")@Nullable T arr){
        if(notNull(arr) && !arr.getClass().isArray()){
            return false;
        }
        return (isNull(arr) || Array.getLength(arr)==0);
    }

    @Remark("{collections} is exists empty or null")
    public static boolean isExEmptyCollection(@Name("collections")@Nullable Collection<?> ... collections){
        if(isEmptyArray(collections)){
            return false;
        }
        for(Collection<?> item : collections){
            if(isEmptyCollection(item)){
                return true;
            }
        }
        return false;
    }

    @Remark("{collection} is empty or null")
    public static<T> boolean isEmptyCollection(@Name("collection")@Nullable Collection<T> collection){
        return (isNull(collection) || collection.size()==0);
    }

    @Remark("{maps} exists emprty map or empty")
    public static boolean isExEmptyMap(@Name("maps")@Nullable Map<?,?> ... maps){
        if(isEmptyArray(maps)){
            return false;
        }
        for(Map<?,?> item : maps){
            if(isEmptyMap(item)){
                return true;
            }
        }
        return false;
    }

    @Remark("{map} is empty or null")
    public static<T,E> boolean isEmptyMap(@Name("map")@Nullable Map<T,E> map){
        return (isNull(map) || map.size()==0);
    }
    @Remark("{obj} not null")
    public static boolean notNull(@Name("obj")@Nullable Object obj){
        return !isNull(obj);
    }

    public static boolean notEmptyStr(@Nullable String str, boolean needTrimed){
        return !isEmptyStr(str,needTrimed);
    }

    public static<T> boolean notEmptyArray(@Nullable T[] arr){
        return !isEmptyArray(arr);
    }

    public static<T> boolean notEmptyCollection(@Nullable Collection<T> collection){
        return !isEmptyCollection(collection);
    }
    public static<T,E> boolean notEmptyMap(@Nullable Map<T,E> map){
        return !isEmptyMap(map);
    }

    public static boolean isExEmpty(@Nullable Object ... objs){
        if(isEmptyArray(objs)){
            return false;
        }
        for(Object item : objs){
            if(isEmpty(item)){
                return true;
            }
        }
        return false;
    }

    public static<T> boolean isEmpty(@Nullable T obj){
        if(notNull(obj) && obj.getClass().isArray()){
            return isEmptyArray(obj);
        }else if(obj instanceof Map<?,?>){
            return isEmptyMap((Map) obj);
        } else if(obj instanceof Collection<?>){
            return isEmptyCollection((Collection) obj);
        }else if(obj instanceof String){
            return isEmptyStr((String)obj,false);
        }
        return isNull(obj);
    }

    public static final int COMPARE_RESULT_UPPER=1;
    public static final int COMPARE_RESULT_LOWER=-1;
    public static final int COMPARE_RESULT_EQUAL=0;
    protected static boolean isNumOpe(Number num1,Number num2,int ... compareResults){
        BigDecimal bnum1=(num1 instanceof BigDecimal?(BigDecimal)num1: new BigDecimal(String.valueOf(num1)));
        BigDecimal bnum2=(num2 instanceof BigDecimal?(BigDecimal)num2: new BigDecimal(String.valueOf(num2)));
        int rs=bnum1.compareTo(bnum2);
        if(rs<COMPARE_RESULT_EQUAL){
            rs=COMPARE_RESULT_LOWER;
        }
        if(rs>COMPARE_RESULT_EQUAL){
            rs=COMPARE_RESULT_UPPER;
        }
        for(int item : compareResults){
            if(rs==item){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumLower(Number targetNum, Number cmpNum){
        return isNumOpe(targetNum,cmpNum,COMPARE_RESULT_LOWER);
    }

    public static boolean isExNumLower(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumLower(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumGather(Number targetNum, Number cmpNum){
        return isNumOpe(targetNum,cmpNum,COMPARE_RESULT_UPPER);
    }

    public static boolean isExNumGather(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumGather(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumLowerEqu(Number targetNum,Number cmpNum){
        return isNumOpe(targetNum,cmpNum,COMPARE_RESULT_EQUAL,COMPARE_RESULT_LOWER);
    }

    public static boolean isExNumLowerEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumLowerEqu(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumGatherEqu( Number targetNum,Number cmpNum){
        return isNumOpe(targetNum,cmpNum,COMPARE_RESULT_EQUAL,COMPARE_RESULT_UPPER);
    }

    public static boolean isExNumGatherEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumGatherEqu(item,cmpNum)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenBoth(Number targetNum,Number min,Number max){
        return isNumGatherEqu(targetNum,min) && isNumLowerEqu(targetNum,max);
    }

    public static boolean isExNumBetweenBoth(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenBoth(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenLeft(Number targetNum,Number min,Number max){
        return isNumGatherEqu(targetNum,min) && isNumLower(targetNum,max);
    }

    public static boolean isExNumBetweenLeft(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenLeft(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isNumBetweenOpen(Number targetNum,Number min,Number max){
        return isNumGather(targetNum,min) && isNumLower(targetNum,max);
    }

    public static boolean isExNumBetweenOpen(Number min,Number max, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumBetweenOpen(item,min,max)){
                return true;
            }
        }
        return false;
    }

    public static boolean isExNumEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(isNumOpe(item,cmpNum,COMPARE_RESULT_EQUAL)){
                return true;
            }
        }
        return false;
    }

    public static boolean isExNumNotEqu(Number cmpNum, Number ... nums){
        if(isEmptyArray(nums)){
            return false;
        }
        for(Number item : nums){
            if(!isNumOpe(item,cmpNum,COMPARE_RESULT_EQUAL)){
                return true;
            }
        }
        return false;
    }

    //一些常用的正则表达式串，这些串都可以作为参数进行匹配
    public static final String REGEX_INT_NUMBER="^[+|-]?[1-9]\\d*$";
    public static final String REGEX_FLOAT_NUMBER="^[+|-]?[1-9]\\d*(\\.\\d+)?|[+|-]?0(\\.\\d+)?$";
    public static final String REGEX_SCIENTIFIC_NUMBER="^[+|-]?[1-9]\\d*(\\.\\d+)?([E|e][+|-]?[1-9]\\d*)|[+|-]?0(\\.\\d+)?([E|e][+|-]?[1-9]\\d*)$";
    public static final String REGEX_MOBILE_11="^(\\+[0-9]{2}\\s*)?[1][0-9]{10}$";
    public static final String REGEX_EMAIL="^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    public static final String REGEX_ID_NUMBER="^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$";
    public static final String REGEX_URL="^[a-zA-Z]+://[^\\s]*$";
    //允许匹配的日期时间格式
    //1-1-1 23:59:59 666
    //9999-12-31 00:00:00 000
    //2020年08月06日12时35分16秒3豪
    //所有位置均为1个字符占位即可
    //段内分隔符不允许空白符
    //段间分隔允许空白符
    //yyyy-MM-dd
    public static final String REGEX_DATE="^[1-9]([0-9]{0,3})?([\\S|\\D])(0?[1-9]|1[0-2])([\\S|\\D])(0?[1-9]|[1-2][0-9]|3[0-1])$";
    //HH:mm:ss SSS
    //HH:mm:ss
    public static final String REGEX_TIME="^(0?[0-9]|1[0-9]|2[0-3])([\\S|\\D])[0-5]?[0-9]([\\S|\\D])[0-5]?[0-9](([\\D])[0-9]{0,3})?$";
    //yyyy-MM-dd HH:mm:ss SSS
    //yyyy-MM-dd HH:mm:ss
    public static final String REGEX_DATE_TIME="^[1-9]([0-9]{0,3})?([\\S|\\D])(0?[1-9]|1[0-2])([\\S|\\D])(0?[1-9]|[1-2][0-9]|3[0-1])([\\D])(0?[0-9]|1[0-9]|2[0-3])([\\S|\\D])[0-5]?[0-9]([\\S|\\D])[0-5]?[0-9](([\\D])[0-9]{0,3})?$";
    /**
     * 匹配字符串，通过正则
     * @param str 字符串
     * @param regex 正则
     * @return 是否满足正则
     */
    public static boolean isMatched(String str,String regex){
        if(isExNull(str,regex)){
            return false;
        }
        return str.matches(regex);
    }
    public static boolean exNotMatched(String regex,String ... strs){
        if(isEmptyArray(strs)){
            return false;
        }
        for(String item : strs){
            if(notMatched(item,regex)){
                return true;
            }
        }
        return false;
    }

    public static boolean notMatched(String str,String regex){
        return !isMatched(str,regex);
    }
    public static boolean isIntNumber(String str){
        return isMatched(str,REGEX_INT_NUMBER);
    }
    public static boolean isFloatNumber(String str){
        return isMatched(str,REGEX_FLOAT_NUMBER);
    }
    public static boolean isScientificNumber(String str){
        return isMatched(str,REGEX_SCIENTIFIC_NUMBER);
    }
    public static boolean isPhoneNumber(String str){
        return isMatched(str,REGEX_MOBILE_11);
    }
    public static boolean isEmailAddr(String str){
        return isMatched(str,REGEX_EMAIL);
    }

    public static boolean notIntNumber(String str){
        return !isIntNumber(str);
    }
    public static boolean notFloatNumber(String str){
        return !isFloatNumber(str);
    }
    public static boolean notScientificNumber(String str){
        return !isScientificNumber(str);
    }
    public static boolean notPhoneNumber(String str){
        return !isPhoneNumber(str);
    }
    public static boolean notEmailAddr(String str){
        return !isEmailAddr(str);
    }

    public static boolean isEquals(boolean deep,Object obj1,Object obj2){
        if(obj1==obj2){
            return true;
        }
        if(isExNull(obj1,obj2)){
            return false;
        }
        if(deep){
            return isDeepEquals(obj1, obj2);
        }
        return obj1.equals(obj2);
    }
    public static boolean isDeepEquals(Object obj1,Object obj2){
        Class clazzObj1=obj1.getClass();
        Class clazzObj2=obj2.getClass();
        if(obj1 instanceof Map && obj2 instanceof Map){
            return deepEqualsMap((Map)obj1,(Map)obj2);
        }
        if(obj1 instanceof Collection && obj2 instanceof Collection){
            return deepEqualsCollection((Collection)obj1,(Collection)obj2);
        }
        if(clazzObj1.isArray() && clazzObj2.isArray()){
            return deepEqualsArray(obj1, obj2);
        }
        return obj1.equals(obj2);
    }
    public static boolean deepEqualsCollection(Collection col1,Collection col2){
        if(col1.size()!=col2.size()){
            return false;
        }
        Iterator it1=col1.iterator();
        Iterator it2=col2.iterator();
        while (it1.hasNext()){
            Object o1=it1.next();
            Object o2=it2.next();
            boolean eqs=isDeepEquals(o1,o2);
            if(!eqs){
                return false;
            }
        }
        return true;
    }
    public static boolean deepEqualsMap(Map map1,Map map2){
        if(map1.size()!=map2.size()){
            return false;
        }
        Set st1= map1.keySet();
        Set st2= map2.keySet();
        if(!deepEqualsCollection(st1,st2)){
            return false;
        }
        for(Object item : st1){
            Object o1=map1.get(item);
            Object o2=map2.get(item);
            boolean eqs=isDeepEquals(o1,o2);
            if(!eqs){
                return false;
            }
        }
        return true;
    }
    public static boolean deepEqualsArray(Object obj1,Object obj2){
        Class clazzObj1=obj1.getClass();
        Class clazzObj2=obj2.getClass();
        if(clazzObj1.isArray() && clazzObj2.isArray()){
            int len=Array.getLength(obj1);
            if(len!=Array.getLength(obj2)){
                return false;
            }
            for (int i = 0; i < len; i++) {
                Object p1=Array.get(obj1,i);
                Object p2=Array.get(obj2,i);
                boolean eqs=isDeepEquals(p1,p2);
                if(!eqs){
                    return false;
                }
            }
            return true;
        }
        return isDeepEquals(obj1, obj2);
    }
}
