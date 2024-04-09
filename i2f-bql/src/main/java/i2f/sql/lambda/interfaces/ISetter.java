package i2f.sql.lambda.interfaces;

/**
 * @author Ice2Faith
 * @date 2024/4/8 16:27
 * @desc
 */
@FunctionalInterface
public interface ISetter<T, V> extends ILambda {
    void set(T obj, V val) throws Throwable;
}
