package i2f.core.proxy;

import i2f.core.annotations.notice.Name;
import i2f.core.annotations.remark.Author;
import i2f.core.annotations.remark.Remark;

/**
 * @author ltb
 * @date 2022/3/25 20:29
 * @desc
 */
@Author("i2f")
@Remark("defined proxy hook handler")
public interface IProxyHandler {
    @Remark({
            "provide an method for you can set some context argument when once proxy",
            "such statistic invoke use time or other situation"
    })
    Object initContext();
    /**
     * 在调用之前Hook
     * 返回值不为null,则表示提前返回，不再执行代理对象的调用
     * @param ivkObj
     * @param invokable
     * @param args
     * @return
     */
    @Remark("hook on before invoke,when return value not null,will early return rather than truth invoke.")
    Object before(@Name("context") Object context,@Name("ivkObj") Object ivkObj, @Name("invokable") IInvokable invokable, @Name("args") Object ... args);

    /**
     * 在调用之后Hook，返回值为调用返回后的值
     * 一般情况下，返回值应该和第三个入参retVal一致
     * @param ivkObj
     * @param invokable
     * @param retVal
     * @param args
     * @return
     */
    @Remark("hook on after invoke,usually,return value should equals retVal.")
    Object after(@Name("context") Object context,@Name("ivkObj") Object ivkObj,@Name("invokable") IInvokable invokable,@Name("retVal") Object retVal,Object ... args);

    /**
     * 在调用发生异常时Hook，返回值为调用抛出的异常对象
     * 一般情况下，返回值应该和第三个入参ex一致
     * @param ivkObj
     * @param invokable
     * @param ex
     * @param args
     * @return
     */
    @Remark("hook on exception raise,usually,return value should equals ex.")
    Throwable except(@Name("context") Object context,@Name("ivkObj") Object ivkObj,@Name("invokable") IInvokable invokable,@Name("ex") Throwable ex,Object ... args);
}
