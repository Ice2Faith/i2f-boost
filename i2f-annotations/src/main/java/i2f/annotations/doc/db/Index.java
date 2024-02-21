package i2f.annotations.doc.db;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "索引",
        "用于表示当前字段属于索引index，用order表示索引中的排序，compare表示索引方向（升序，降序），type表示索引类型（hash,tree）",
        "组合索引就是对多个字段标注@Index，通过value进行关联",
        "例如：索引 idx_status_sex(status,sex) 是一个组合索引",
        "则就有分别对status和sex字段的两个注解分别为：",
        "@Index(value=\"idx_status_sex\",order=0) private Integer status;",
        "@Index(value=\"idx_status_sex\",order=1) private Integer sex;"
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
public @interface Index {
    String value();
    int order() default -1;
    OrderMode compare() default OrderMode.DEFAULT;
    String type() default "";
}
