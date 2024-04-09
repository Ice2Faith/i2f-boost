package i2f.sql.lambda;

/**
 * @author Ice2Faith
 * @date 2024/4/9 11:51
 * @desc
 */
public interface IClassTableNameResolver {
    String getName(Class<?> clazz);
}
