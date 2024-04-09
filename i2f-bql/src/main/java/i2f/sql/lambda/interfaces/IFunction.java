package i2f.sql.lambda.interfaces;

/**
 * @author Ice2Faith
 * @date 2024/4/8 16:27
 * @desc
 */
@FunctionalInterface
public interface IFunction<T, V, R> extends ILambda {
    R apply(T obj, V val) throws Throwable;
}
