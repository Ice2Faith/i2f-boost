package i2f.core.zplugin.validate.impl;

import i2f.core.reflect.convert.ConvertResolver;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 15:23
 * @desc
 */
public class TypeValidator extends AbstractValidator {
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        this.message="not matched type";
        if(obj==null){
            return true;
        }
        VNotType type=getAnnotation(traceAnnotations,VNotType.class);
        if(type==null){
            return true;
        }
        Class ckType=obj.getClass();
        Class[] tarTypes=type.value();
        return ConvertResolver.isInTypes(ckType,tarTypes);
    }

    @Override
    public String message() {
        return message;
    }
}
