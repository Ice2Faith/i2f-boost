package i2f.annotations.core.transaction;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "事务型的",
        "用于表示处于事务中或者需要在事务中执行",
        "针对常见的jdbc的事务，提供几个参数进行控制，value作为参数，type作为事务类型，level作为隔离级别"
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
public @interface Transaction {
    String value() default "";
    String type() default "";
    String level() default "";
}
