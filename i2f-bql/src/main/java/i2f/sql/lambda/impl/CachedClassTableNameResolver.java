package i2f.sql.lambda.impl;

import i2f.sql.lambda.IClassTableNameResolver;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Ice2Faith
 * @date 2024/4/9 9:02
 * @desc
 */
public class CachedClassTableNameResolver implements IClassTableNameResolver {
    protected boolean upperCase = false;
    protected Function<Class<?>, String> nameResolver;
    protected ConcurrentHashMap<Class<?>, String> fastNameMap = new ConcurrentHashMap<>();

    public CachedClassTableNameResolver(Function<Class<?>, String> nameResolver) {
        this.nameResolver = nameResolver;
    }

    public CachedClassTableNameResolver(boolean upperCase, Function<Class<?>, String> nameResolver) {
        this.upperCase = upperCase;
        this.nameResolver = nameResolver;
    }

    @Override
    public String getName(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class catch name error!");
        }
        String name = fastNameMap.get(clazz);
        if (name != null) {
            return name;
        }
        name = nameResolver.apply(clazz);
        if (upperCase) {
            name = name.toUpperCase();
        }
        fastNameMap.put(clazz, name);
        return name;
    }
}
