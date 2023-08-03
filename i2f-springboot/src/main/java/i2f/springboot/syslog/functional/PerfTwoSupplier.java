package i2f.springboot.syslog.functional;

/**
 * 提供双参数的泛型支持
 *
 * @param <T>
 * @param <V1>
 * @param <V2>
 */
@FunctionalInterface
public interface PerfTwoSupplier<T, V1, V2> {
    T get(V1 v1, V2 v2);
}