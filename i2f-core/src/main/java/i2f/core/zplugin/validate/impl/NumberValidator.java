package i2f.core.zplugin.validate.impl;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 16:21
 * @desc
 */
public class NumberValidator extends AbstractValidator{
    @Override
    public boolean valid(Object obj, List<Annotation> traceAnnotations) {
        this.message="number not in valid range";
        if(obj == null){
            return false;
        }
        if(!(obj instanceof Number)){
            return false;
        }
        VNumber ann=getAnnotation(traceAnnotations,VNumber.class);
        if(ann==null){
            return true;
        }
        BigDecimal num=new BigDecimal(String.valueOf(obj));
        if(!"".equals(ann.min())){
            BigDecimal min=new BigDecimal(ann.min());
            if(num.compareTo(min)<0){
                this.message="num require gather/equal "+min;
                return false;
            }
        }
        if(!"".equals(ann.max())){
            BigDecimal max=new BigDecimal(ann.max());
            if(num.compareTo(max)>0){
                this.message="num require lower/equal "+max;
                return false;
            }
        }

        return true;
    }
}
