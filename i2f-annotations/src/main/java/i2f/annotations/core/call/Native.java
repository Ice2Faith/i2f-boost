package i2f.annotations.core.call;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "本地的",
        "表示调用的是本地Native方法，或者用于辅助标记Native方法的来源"
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
public @interface Native {
    String value() default "";
    String module() default "";
    String type() default "";
    Class<?> clazz() default Object.class;
}
