package i2f.core.zplugin.inject.core;

import i2f.core.iterator.Iterators;
import i2f.core.reflect.ValueResolver;
import i2f.core.str.Strings;
import i2f.core.zplugin.inject.IInjectFieldProvider;
import i2f.core.zplugin.inject.annotations.Injects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/5/9 15:45
 * @desc
 */
public class InjectProvider {
    public static volatile InjectProvider globalProvider=new InjectProvider();
    public static InjectProvider provider(){
        return globalProvider;
    }
    //////////////////////////////////////////////////////////
    protected volatile ConcurrentHashMap<Class<IInjectFieldProvider>,ConcurrentHashMap<String,IInjectFieldProvider>> context=new ConcurrentHashMap<>();
    public InjectProvider registry(IInjectFieldProvider provider){
        Class<IInjectFieldProvider> clazz=(Class<IInjectFieldProvider>)provider.getClass();
        String className = clazz.getSimpleName();
        String beanName = Strings.firstLowerCase(className);
        return registry(beanName,provider);
    }
    public InjectProvider registry(String name,IInjectFieldProvider provider){
        Class<IInjectFieldProvider> clazz=(Class<IInjectFieldProvider>)provider.getClass();
        if(!context.containsKey(clazz)){
            context.put(clazz,new ConcurrentHashMap<>());
        }
        context.get(clazz).put(name,provider);
        return this;
    }

    //////////////////////////////////////////////////////////

    public List<IInjectFieldProvider> getFieldProvidersByNames(String ... names){
        List<IInjectFieldProvider> ret=new ArrayList<>();
        for(String name : names){
            for(ConcurrentHashMap<String,IInjectFieldProvider> item : context.values()){
                for(Map.Entry<String,IInjectFieldProvider> define : item.entrySet()){
                    if(define.getKey().equals(name)){
                        ret.add(define.getValue());
                    }
                }
            }
        }
        return ret;
    }
    public List<IInjectFieldProvider> getFieldProvidersByClasses(Class<IInjectFieldProvider> ... classes){
        List<IInjectFieldProvider> ret=new ArrayList<>();
        for(Class item : classes){
            for(Class<IInjectFieldProvider> key : context.keySet()){
                if(key.equals(item)){
                    for(IInjectFieldProvider define : context.get(key).values()){
                        ret.add(define);
                    }
                }
            }
        }
        return ret;
    }
    public<T> T inject(T obj, Injects ann){
        List<IInjectFieldProvider> providers=new ArrayList<>();
        String[] beanNames= ann.providerNames();
        List<IInjectFieldProvider> namesProviders=getFieldProvidersByNames(beanNames);
        providers.addAll(namesProviders);

        Class<IInjectFieldProvider>[] classes=ann.providerClasses();
        List<IInjectFieldProvider> classesProviders=getFieldProvidersByClasses(classes);
        providers.addAll(classesProviders);

        return inject(obj,providers,ann.fields());
    }
    public<T> T inject(T obj,Iterable<IInjectFieldProvider> providers,String ... fields){
        return inject(obj, providers, Iterators.of(fields));
    }
    public<T> T inject(T obj, Iterable<IInjectFieldProvider> providers, Iterator<String> fields){
        while(fields.hasNext()){
            String fieldName= fields.next();
            boolean hit=false;
            for(IInjectFieldProvider item : providers){
                try{
                    Object val=item.getValue(fieldName);
                    if(val!=null){
                        setValue(obj,fieldName,val);
                        hit=true;
                        break;
                    }
                }catch(Exception e){

                }
            }
            // 未命中提供者，则说明为null值返回，则设置null值
            if(!hit){
                setValue(obj,fieldName,null);
            }
        }
        return obj;
    }
    public void setValue(Object obj,String fieldName,Object val){
        ValueResolver.set(obj,fieldName,val);
    }
}
