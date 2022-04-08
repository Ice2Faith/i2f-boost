package i2f.core.zplugin.validate.impl;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 16:21
 * @desc
 */
public class RegexMatchValidator extends AbstractValidator{
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        this.message="string not match regex";
        if(obj == null){
            return false;
        }
        VNotMatch ann=getAnnotation(traceAnnotations,VNotMatch.class);
        if(ann==null){
            return true;
        }
        if("".equals(ann.value())){
            return true;
        }
        String str=String.valueOf(obj);

        return str.matches(ann.value());
    }
}
