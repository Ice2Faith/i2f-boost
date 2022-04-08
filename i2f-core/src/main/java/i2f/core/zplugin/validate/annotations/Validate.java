package i2f.core.zplugin.validate.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.zplugin.validate.IValidator;

import java.lang.annotation.*;

/**
 * @author ltb
 * @date 2022/3/28 8:57
 * @desc
 */
@Author("i2f")
@Remark({
        "using to point custom validators for validate arguments or other object",
        "when using on method then check method return value"
})
@Target({
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD
})
@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    @Remark("when validator return value equal this value will be bad recognize")
    boolean value() default false;
    Class<? extends IValidator>[] validators() default {};
}
