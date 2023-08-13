package i2f.std.invoke;

/**
 * @author Ice2Faith
 * @date 2023/8/3 14:59
 * @desc
 */
public interface IArgsInvokable extends IInvokable {
    Object[] getArgs();
    Object invoke(Object[] args) throws Throwable;
}
