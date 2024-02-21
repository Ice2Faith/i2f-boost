package i2f.annotations.core.base;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "值来源引用",
        "用于表示这个值的取值来自那个类，常见的就是某个类中定义了一系列的常量表示，但是在调用时是基本数据类型",
        "使用value指定来自那个类，另外可以使用like来指定一些列的特征的属性名",
        "@Refer(value=TypeConsts.clazz,like=\"TYPE_*\") int type"
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
public @interface Refer {
    Class<?> value();
    String like() default "";
}
