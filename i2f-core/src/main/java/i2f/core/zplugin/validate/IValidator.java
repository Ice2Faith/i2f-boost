package i2f.core.zplugin.validate;

import i2f.core.annotations.remark.Remark;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author ltb
 * @date 2022/3/28 9:00
 * @desc
 */
public interface IValidator {
    /**
     * traceAnnotations 表示你的自定义注解，一直往上找注解的注解，一直找到根注解 Validate 经过的路径
     * VProxy --> VNotNull --> Validate
     * 就表示，触发的直接注解是VProxy,最终注解是Validate
     * VProxy注解自身上有VNotNull注解
     * VNotNUll注解自身上与Validate注解
     * @param obj
     * @param traceAnnotations
     * @return
     */
    @Remark({
            "trace annotations is annotation who parent is Validate tree route list",
            "such: VProxy --> VNotNull --> Validate ",
            "form that @Validate @VNotNull @VProxy",
            "VProxy is tree leaf",
            "Validate is tree end root",
            "list is leaf to root path,and include leaf nd root",
            "provide this annotation for you can get special value form them to process specific logical"
    })
    boolean valid(@Remark("wait valid object") Object obj,
                  @Remark("trigger annotations parent list") List<Annotation> traceAnnotations);
    String message();
}
