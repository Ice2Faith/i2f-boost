package i2f.annotations.doc.api;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "方式、方法",
        "在API中，用于指定支持的请求方式，在HTTP中则表示例如：POST、GET、PUT、DELETE这样的，其他的根据协议不同而定",
        "在SpringMVC中，一般用于Mapping方法"
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
public @interface Method {
    String[] value();
}
