package i2f.core.jdbc.sql.core;

import i2f.core.db.annotations.DbName;
import i2f.core.lambda.Lambdas;
import i2f.core.lambda.functional.preset.IBeanBuilder;
import i2f.core.lambda.functional.preset.IBeanGetter;
import i2f.core.lambda.functional.preset.IBeanSetter;
import i2f.core.reflect.core.ReflectResolver;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/5/23 14:44
 * @desc
 */
public class DbFinder {
    public static volatile ConcurrentHashMap<String,Class> cacheClasses=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<String,String> cacheFieldName2DbName=new ConcurrentHashMap<>();
    public static volatile ConcurrentHashMap<Class,String> cacheClassName2DbName=new ConcurrentHashMap<>();

    public static <T, R> String dbFieldName(IBeanGetter<T, R> getter) {
        return dbFileNameProxy(getter);
    }

    public static <T, V1> String dbFieldName(IBeanSetter<T, V1> getter) {
        return dbFileNameProxy(getter);
    }

    public static <R, T, V1> String dbFieldName(IBeanBuilder<R, T, V1> getter) {
        return dbFileNameProxy(getter);
    }

    public static String dbFileNameProxy(Serializable lmb) {
        SerializedLambda meta = Lambdas.getSerializedLambdaNoExcept(lmb);
        String className = meta.getImplClass();
        className = className.replaceAll("/", ".");
        String methodName = meta.getImplMethodName();
        if (!cacheClasses.containsKey(className)) {
            cacheClasses.put(className, ReflectResolver.getClazz(className));
        }
        Class clazz = cacheClasses.get(className);
        String fieldName = Lambdas.fieldName(meta);
        String fullFieldName = className + "." + fieldName;
        if(cacheFieldName2DbName.containsKey(fullFieldName)){
            return cacheFieldName2DbName.get(fullFieldName);
        }
        Field field = ReflectResolver.findField(clazz, fieldName);
        DbName ann=ReflectResolver.findAnnotation(field,DbName.class,false);
        String dbName=fieldName;
        if(ann!=null){
            if(!"".equals(ann.value())){
                dbName=ann.value();
            }
        }
        cacheFieldName2DbName.put(fullFieldName,dbName);
        return dbName;
    }
    public static String dbTableName(Class clazz){
        if(cacheClassName2DbName.containsKey(clazz)){
            return cacheClassName2DbName.get(clazz);
        }
        DbName ann=ReflectResolver.findAnnotation(clazz,DbName.class,false);
        String dbName=clazz.getSimpleName();
        if(ann!=null){
            if(!"".equals(ann.value())){
                dbName=ann.value();
            }
        }
        cacheClassName2DbName.put(clazz,dbName);
        return dbName;
    }
}
