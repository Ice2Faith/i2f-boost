package i2f.annotations.doc.base;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "更新人",
        "用于表示更新人以及更新的相关信息"
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
public @interface Updater {
    Author value();
    CreateTime time();
    Comments comment() default @Comments();
    Descriptions description() default @Descriptions();
    Email email() default @Email({});
    Link link() default @Link({});
}
