package i2f.annotations.core.dict;

import i2f.annotations.doc.base.Comment;

import java.lang.annotation.*;

/**
 * @author Ice2Faith
 * @date 2024/2/21 8:51
 * @desc
 */
@Comment({
        "字典翻译到",
        "用于将当前字段的字典值翻译到目标字段",
        "num -> str",
        "默认从当前字段查找字典列表",
        "当field指定时，则从当前类的field字段查找字典",
        "当clazz指定时，则从clazz的field字段查找字典",
        "同理，method与field理论一致",
        "只不过要求method返回Collection<IDict>类型的一个get方法",
        "限制为本对象上或者本类的静态函数",
        "同理，如果一类枚举类实现了IDict接口，则使用enums指定为这个枚举类",
        "同样也是可以得到字典的",
        "在某些场景中，可能不一定使用code作为字典值，而是使用key，这时，将useKey置为true即可"
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
public @interface DictDecodeTo {
    String value();
    Class<?> clazz() default Object.class;
    String field() default "";
    String method() default "";
    Class<?> enums() default Object.class;
    boolean useKey() default false;
}
