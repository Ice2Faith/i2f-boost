package i2f.sql.lambda.impl;

import i2f.sql.annotations.DbName;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:07
 * @desc
 */
public class DefaultFieldColumnNameResolver extends CachedFieldColumnNameResolver {

    public static final Function<Field, String> DEFAULT_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        DbName ann = NameResolver.getAnnotation(field, DbName.class);
        if (ann == null) {
            return NameResolver.toUnderScore(name);
        }
        String value = ann.value();
        if (value != null && !"".equals(value)) {
            return value;
        }
        return NameResolver.toUnderScore(name);
    };

    public static final Function<Field, String> ANNOTATION_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        DbName ann = NameResolver.getAnnotation(field, DbName.class);
        if (ann == null) {
            return name;
        }
        String value = ann.value();
        if (value != null && !"".equals(value)) {
            return value;
        }
        return name;
    };

    public static final Function<Field, String> UNDERSCORE_NAME_RESOLVER = (field) -> {
        String name = field.getName();
        return NameResolver.toUnderScore(name);
    };

    public static final DefaultFieldColumnNameResolver DEFAULT = new DefaultFieldColumnNameResolver(DEFAULT_NAME_RESOLVER);
    public static final DefaultFieldColumnNameResolver ANNOTATION = new DefaultFieldColumnNameResolver(ANNOTATION_NAME_RESOLVER);
    public static final DefaultFieldColumnNameResolver UNDERSCORE = new DefaultFieldColumnNameResolver(UNDERSCORE_NAME_RESOLVER);

    public DefaultFieldColumnNameResolver(Function<Field, String> nameResolver) {
        super(nameResolver);
    }

    public DefaultFieldColumnNameResolver(boolean upperCase, Function<Field, String> nameResolver) {
        super(upperCase, nameResolver);
    }

}
