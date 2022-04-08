package i2f.core.zplugin.validate.impl;

import i2f.core.reflect.core.ReflectResolver;
import i2f.core.reflect.interfaces.PropertyAccessor;
import i2f.core.zplugin.validate.IValidator;
import i2f.core.zplugin.validate.annotations.Validate;
import i2f.core.zplugin.validate.core.ValidateProvider;
import i2f.core.zplugin.validate.exception.ValidateException;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 10:28
 * @desc
 */
public class BeanValidator implements IValidator {
    private String message="bean validate failure";
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        Class clazz=obj.getClass();
        List<PropertyAccessor> accessors= ReflectResolver.getLogicalReadableFieldsWithAnnotations(clazz,true, Validate.class);
        boolean bad=false;
        for(PropertyAccessor item : accessors){
            if(item.getField()==null){
                continue;
            }
            item.setInvokeObject(obj);
            Object val=item.get();
            try{
                ValidateProvider.valid(item.getField(),val);
            }catch(ValidateException e){
                this.message=e.getMessage();
                bad=true;
                break;
            }
        }
        return bad;
    }

    @Override
    public String message() {
        return "BeanValidator:"+message;
    }
}
