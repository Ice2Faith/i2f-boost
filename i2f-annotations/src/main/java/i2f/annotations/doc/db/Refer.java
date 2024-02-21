package i2f.annotations.doc.db;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "参考、对照",
        "用于表示本字段对应的其他字段，一般用于字典的来源说明，或者在不使用外键的体系中，表示字段来源",
        "例如，本字段是字典字段，则表示本字典的来源是table表的column字段",
        "或者本字段是来源于其他表的，例如，明细单的[主单ID]来源于主单的[ID]"
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
    String table();
    String column();
    Class<?> clazz() default Object.class;
}
