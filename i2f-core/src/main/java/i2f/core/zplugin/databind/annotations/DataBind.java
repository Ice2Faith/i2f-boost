package i2f.core.zplugin.databind.annotations;

import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.annotations.remark.Usage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author ltb
 * @date 2022/3/23 21:26
 * @desc 数据绑定注解
 * 从bind指定的参数中，获取value指定的路径表达式的变量
 * 作为数据绑定的值
 * 当注解作用在参数上、类上、属性上时，bind值被忽略，因为被绑定的参数是唯一确认的
 */
@Author("i2f")
@Target({
        ElementType.FIELD,
        ElementType.PARAMETER,
        ElementType.METHOD,
        ElementType.TYPE
})
@Usage({
        "@DataBind(value=\"data.name\",bind=\"1\")",
        "public Object getData(String id,UserVo user){}",
        "it means that bind 1 index argument's data.name attribute, equals user.data.name"
})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataBind {
    @Remark("what field cloud be use")
    String value();
    @Remark("which object be bind,cloud be argument name,index,when index is -1 mean's return value")
    String bind() default "";
}
