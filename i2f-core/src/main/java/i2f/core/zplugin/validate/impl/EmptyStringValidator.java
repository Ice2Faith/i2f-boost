package i2f.core.zplugin.validate.impl;

import i2f.core.zplugin.validate.IValidator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 9:04
 * @desc
 */
public class EmptyStringValidator implements IValidator {
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        if(obj==null){
            return true;
        }
        if("".equals(obj)){
            return true;
        }
        return false;
    }

    @Override
    public String message() {
        return "validate cannot be null or empty stringify";
    }
}
