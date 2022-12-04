package i2f.core.zplugin.cache;

import i2f.core.cache.ICache;
import i2f.core.cache.impl.mem.MemCache;
import i2f.core.proxy.IInvokable;
import i2f.core.proxy.impl.BasicProxyHandler;
import i2f.core.proxy.impl.IMethodAccessInvokable;
import i2f.core.reflect.core.ReflectResolver;
import i2f.core.str.Appender;
import i2f.core.zplugin.cache.annotations.Cached;
import i2f.core.zplugin.databind.DatabindResolver;
import i2f.core.zplugin.databind.annotations.DataBind;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author ltb
 * @date 2022/6/6 9:35
 * @desc
 */
public class CacheProxyHandler extends BasicProxyHandler {
    protected ICache<String, Object> cache;

    public CacheProxyHandler() {
        cache = new MemCache<>();
    }

    public CacheProxyHandler(ICache<String, Object> cache) {
        this.cache = cache;
    }

    @Override
    public Object initContext() {
        return System.currentTimeMillis();
    }

    @Override
    public Object before(Object context, Object ivkObj, IInvokable invokable, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return null;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        Parameter[] params= method.getParameters();
        Cached ann= ReflectResolver.findAnnotation(method,Cached.class,false);
        if(ann==null ||!ann.value()){
            return null;
        }
        DataBind[] binds=ann.binds();
        Object[] vals=new Object[binds.length];
        for (int i = 0; i < binds.length; i++) {
            try{
                vals[i]= DatabindResolver.getMethodBindValue(binds[i],method,args);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        String baseKey=method.toGenericString();
        if(!"".equals(ann.key())){
            baseKey=ann.key();
        }
        String key=generateKey(baseKey,vals);
        if(cache.exists(key)){
            return cache.get(key);
        }
        return null;
    }

    @Override
    public Object after(Object context, Object ivkObj, IInvokable invokable, Object retVal, Object... args) {
        if(!(invokable instanceof IMethodAccessInvokable)){
            return null;
        }
        IMethodAccessInvokable invoker=(IMethodAccessInvokable)invokable;
        Method method= invoker.method();
        Parameter[] params= method.getParameters();
        Cached ann= ReflectResolver.findAnnotation(method,Cached.class,false);
        if(ann==null ||!ann.value()){
            return null;
        }
        DataBind[] binds=ann.binds();
        Object[] vals=new Object[binds.length];
        for (int i = 0; i < binds.length; i++) {
            try{
                vals[i]= DatabindResolver.getMethodBindValue(binds[i],method,args);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        String baseKey=method.toGenericString();
        if(!"".equals(ann.key())){
            baseKey=ann.key();
        }
        String key=generateKey(baseKey,vals);
        long expire= ann.expire();
        TimeUnit timeUnit=ann.unit();
        if(expire>=0){
            cache.set(key, retVal, expire, timeUnit);
        }else{
            cache.set(key,retVal);
        }

        if(ann.cleans()!=null && ann.cleans().length>0){
            String[] cleanKeys=ann.cleans();
            cleanKeys(cache,cleanKeys);
        }
        return retVal;
    }

    protected String generateKey(String key,Object ... keys){
        String ret = Appender.builder()
                .add(key)
                .addWhen(keys.length>0,":")
                .addsWhen(keys.length>0,keys)
                .get();
        return ret;
    }

    protected void cleanKeys(ICache<String, Object> cache, String... keys) {
        Set<String> set = cache.keys();
        for (String item : set) {
            for (String key : keys) {
                if (item.startsWith(key)) {
                    cache.remove(item);
                }
            }
        }
    }
}
