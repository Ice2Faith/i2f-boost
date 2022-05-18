package i2f.core.lambda;

import i2f.core.exception.BoostException;
import i2f.core.lambda.funcs.IBuilder;
import i2f.core.lambda.funcs.IGetter;
import i2f.core.lambda.funcs.ISetter;
import i2f.core.lambda.funcs.core.*;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * @author ltb
 * @date 2022/5/17 14:37
 * @desc 直接通过引用方法方式获取方法名或对应的字段名
 * System.out.println(Lambdas.fieldName(SysUser::getAvatar));
 * System.out.println(Lambdas.methodName(SysUser::toString));
 * 结果：
 * avatar
 * toString
 */
public class Lambdas {

    /**
     * 通过getter的方法引用获取字段名
     */
    public static <T,R> String fieldName(IGetter<T,R> fn)  {
        return trimPrefixAndFirstLower(fn,"get","is");
    }

    /**
     * 通过setter的方法引用获取字段名
     */
    public static <T,V> String fieldName(ISetter<T,V> fn)  {
        return trimPrefixAndFirstLower(fn,"set");
    }

    /**
     * 通过builder的方法引用获取字段名
     */
    public static <T, R,V> String fieldName(IBuilder<T,R,V> fn)  {
        return trimPrefixAndFirstLower(fn,"set","build","add");
    }

    public static String trimPrefixAndFirstLower(ILambdaArgs fn,String ... prefixes)  {
        String name=trimPrefix(fn,prefixes);
        return firstLower(name);
    }

    public static String trimPrefix(ILambdaArgs fn,String ... prefixes)  {
        String name=methodNameProxy(fn);
        for(String item : prefixes){
            if(name.startsWith(item)){
                name=name.substring(item.length());
                break;
            }
        }
        return name;
    }

    ///////////////////////////////////////////////////////////

    public static<T,R> String methodName(ILambdaArgs0<T,R> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1> String methodName(ILambdaArgs1<T,R,V1> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2> String methodName(ILambdaArgs2<T,R,V1,V2> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3> String methodName(ILambdaArgs3<T,R,V1,V2,V3> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4> String methodName(ILambdaArgs4<T,R,V1,V2,V3,V4> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4,V5> String methodName(ILambdaArgs5<T,R,V1,V2,V3,V4,V5> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4,V5,V6> String methodName(ILambdaArgs6<T,R,V1,V2,V3,V4,V5,V6> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4,V5,V6,V7> String methodName(ILambdaArgs7<T,R,V1,V2,V3,V4,V5,V6,V7> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4,V5,V6,V7,V8> String methodName(ILambdaArgs8<T,R,V1,V2,V3,V4,V5,V6,V7,V8> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,R,V1,V2,V3,V4,V5,V6,V7,V8,V9> String methodName(ILambdaArgs9<T,R,V1,V2,V3,V4,V5,V6,V7,V8,V9> fn)  {
        return methodNameProxy(fn);
    }


    ///////////////////////////////////////////

    public static<T> String methodName(ILambdaArgsVoid0<T> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1> String methodName(ILambdaArgsVoid1<T,V1> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2> String methodName(ILambdaArgsVoid2<T,V1,V2> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3> String methodName(ILambdaArgsVoid3<T,V1,V2,V3> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4> String methodName(ILambdaArgsVoid4<T,V1,V2,V3,V4> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4,V5> String methodName(ILambdaArgsVoid5<T,V1,V2,V3,V4,V5> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4,V5,V6> String methodName(ILambdaArgsVoid6<T,V1,V2,V3,V4,V5,V6> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4,V5,V6,V7> String methodName(ILambdaArgsVoid7<T,V1,V2,V3,V4,V5,V6,V7> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4,V5,V6,V7,V8> String methodName(ILambdaArgsVoid8<T,V1,V2,V3,V4,V5,V6,V7,V8> fn)  {
        return methodNameProxy(fn);
    }

    public static<T,V1,V2,V3,V4,V5,V6,V7,V8,V9> String methodName(ILambdaArgsVoid9<T,V1,V2,V3,V4,V5,V6,V7,V8,V9> fn)  {
        return methodNameProxy(fn);
    }

    protected static String methodNameProxy(ILambdaArgs fn){
        String name=null;
        try{
            name=methodNameNativeProxy(fn);
        }catch(Exception e){
            throw new BoostException(e.getMessage(),e);
        }
        return name;
    }

    protected static String methodNameNativeProxy(ILambdaArgs fn)throws Exception{
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        return methodName;
    }

    /**
     * 关键在于这个方法
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) throws Exception {
        Method method = fn.getClass().getDeclaredMethod("writeReplace");
        method.setAccessible(Boolean.TRUE);
        SerializedLambda lambda = (SerializedLambda) method.invoke(fn);
        return lambda;
    }

    /**
     * 字符串首字母转小写
     */
    public static String firstLower(String field) {
        return field.substring(0,1).toLowerCase()+field.substring(1);
    }

}
