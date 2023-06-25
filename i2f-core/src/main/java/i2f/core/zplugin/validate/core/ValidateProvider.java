package i2f.core.zplugin.validate.core;

import i2f.core.reflection.reflect.core.ReflectResolver;
import i2f.core.zplugin.validate.IValidator;
import i2f.core.zplugin.validate.annotations.Validate;
import i2f.core.zplugin.validate.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ltb
 * @date 2022/3/28 10:28
 * @desc
 */
public class ValidateProvider {
    protected static volatile ConcurrentHashMap<Class, IValidator> cacheValidators=new ConcurrentHashMap<>();

    public static void valid(AnnotatedElement elem, Object val){
        Map<Validate, List<Annotation>> valids=ReflectResolver.findAnnotationsWithSource(elem, Validate.class,true);
        if(valids.size()==0){
            return;
        }
        for(Validate valid : valids.keySet()){
            for(Class<? extends IValidator> item : valid.validators()){
                IValidator validator=getValidator(item);
                boolean ret=validator.valid(val,valids.get(valid));
                if(ret==valid.value()){
                    throw new ValidateException(validator.message());
                }
            }
        }
    }

    public static IValidator getValidator(Class<? extends IValidator> validatorType){
        if(cacheValidators.containsKey(validatorType)){
            return cacheValidators.get(validatorType);
        }
        IValidator ins= ReflectResolver.instance(validatorType);
        cacheValidators.put(validatorType,ins);
        return ins;
    }

}
