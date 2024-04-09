package i2f.sql.lambda.interfaces;

/**
 * @author Ice2Faith
 * @date 2024/4/8 16:27
 * @desc
 */
@FunctionalInterface
public interface IGetter<T, R> extends ILambda {
    R get(T obj) throws Throwable;
}
