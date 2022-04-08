package i2f.core.zplugin.validate.impl;

import i2f.core.zplugin.validate.IValidator;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 9:04
 * @desc
 */
public class NullValidator implements IValidator {
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        return obj==null;
    }

    @Override
    public String message() {
        return "validate cannot be null";
    }
}
