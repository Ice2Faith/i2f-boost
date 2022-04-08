package i2f.core.proxy;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.notice.Nullable;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;
import i2f.core.annotations.remark.Usage;

/**
 * @author ltb
 * @date 2022/3/25 20:38
 * @desc
 */
@Author("i2f")
@Remark("defined invokable interface")
public interface IInvokable {
    @Usage("Object val=IInvokable.invoke(ivkObj,args);")
    @Remark("use args invoke on ivkObj and return value")
    Object invoke(@Name("ivkObj") @Remark("be invoke object") Object ivkObj,
                  @Name("args") @Remark("invoke arguments") @Nullable Object ... args) throws Throwable;
}
