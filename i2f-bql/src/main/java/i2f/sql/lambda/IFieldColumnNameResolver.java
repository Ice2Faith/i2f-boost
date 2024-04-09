package i2f.sql.lambda;

import java.lang.reflect.Field;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:01
 * @desc
 */
@FunctionalInterface
public interface IFieldColumnNameResolver {
    String getName(Field field);
}
