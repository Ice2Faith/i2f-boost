package i2f.annotations.core.wrap;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "填充",
        "用于填充字符串到指定长度",
        "value指定长度，fill指定使用的填充字符串，mode=0表示居中填充，<0表示左填充，>1表示右填充"
})
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.LOCAL_VARIABLE,
        ElementType.METHOD,

        ElementType.TYPE_PARAMETER,
        ElementType.TYPE,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PACKAGE,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Padding {
    int value() default 0;
    String fill() default " ";
    PaddingMode mode() default PaddingMode.LEFT;
}
