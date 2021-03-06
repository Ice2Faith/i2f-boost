package i2f.extension.mybatis.interceptor.proxy;

import i2f.core.proxy.IProxyHandler;
import i2f.core.reflect.core.ReflectResolver;
import i2f.extension.mybatis.interceptor.proxy.core.MybatisInterceptorProxy;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Invocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/4/4 15:54
 * @desc
 */
public class MybatisInterceptorUtil {
    public static MybatisInterceptorProxy proxy(IProxyHandler handler){
        return new MybatisInterceptorProxyProvider().proxyNative(handler);
    }
    public static Object intercept(Invocation invocation, IProxyHandler handler) throws Throwable{
        MybatisInterceptorProxy proxy=proxy(handler);
        return proxy.invoke(invocation);
    }

    public static String getMappedId(MappedStatement stat){
        return stat.getId();
    }

    private static volatile ConcurrentHashMap<String,Set<Method>> mappedIdMethodCache=new ConcurrentHashMap<>();

    public static Set<Method> getMappedMethods(MappedStatement stat){
        String id= stat.getId();
        if(mappedIdMethodCache.containsKey(id)){
            return mappedIdMethodCache.get(id);
        }
        int idx=id.lastIndexOf(".");
        String className=id.substring(0,idx);
        String methodName=id.substring(idx+1);
        Class clazz=ReflectResolver.getClazz(className);
        Set<Method> methods= ReflectResolver.findMethodsByName(clazz,methodName);

        mappedIdMethodCache.put(id, Collections.unmodifiableSet(methods));
        return mappedIdMethodCache.get(id);
    }

    public static Method getMappedMethod(MappedStatement stat){
        return getMappedMethods(stat).iterator().next();
    }
}
