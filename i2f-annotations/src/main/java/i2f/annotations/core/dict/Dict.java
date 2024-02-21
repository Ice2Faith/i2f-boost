package i2f.annotations.core.dict;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "字典",
        "指定一个字典值，value表示字典值，key表示字典键，text表示翻译值，remark表示备注，tags来添加标签"
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
@Repeatable(Dicts.class)
public @interface Dict {
    int value();
    String key() default "";
    String text() default "";
    String remark() default "";
    String[] tags() default {};
}
