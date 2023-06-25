package i2f.core.lang.lambda.condition;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/9/1 8:49
 * @desc 按照SQL的条件进行定义，同时也适用于其他条件环境
 * 这里的定义，都以Object进行定义，方便拓展为你自己的类型
 * 同时这样也支持了，例如link,oper为枚举类型或者其他复杂类型的支持
 * 同时，value也为Object，支持了对嵌套条件，数组，集合等其他复杂类型的支持
 */
@Data
@NoArgsConstructor
public class Condition {
    public Condition(Object link, Object prefix, Object column, Object oper, Object value) {
        this.link = link;
        this.prefix = prefix;
        this.column = column;
        this.oper = oper;
        this.value = value;
    }

    // 条件连接符，and,or
    public Object link;
    // 对于列而言，可能有前缀，也就是表别名
    public Object prefix;
    // 可能需要的操作列，例如 a=1,则column=[a],oper=[=],value=[1]
    // 但是，操作列可以不需要，例如 exists (...),则column=[null],oper=[exists],value=[(...)]
    public Object column;
    public Object oper;
    public Object value;
}
