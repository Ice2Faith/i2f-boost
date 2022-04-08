package i2f.core.zplugin.validate.impl;

import i2f.core.zplugin.validate.IValidator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 15:30
 * @desc
 */
public abstract class AbstractValidator implements IValidator {
    protected String message="validate failure";

    @Override
    public abstract boolean valid(Object obj, List<Annotation> traceAnnotations);

    @Override
    public String message() {
        return message;
    }

    public<T extends Annotation> T getAnnotation(List<Annotation> trace,Class<T> clazz){
        T ret=null;
        for(Annotation item : trace){
            if(item.annotationType().equals(clazz)){
                ret=(T)item;
                break;
            }
        }
        return ret;
    }
}
