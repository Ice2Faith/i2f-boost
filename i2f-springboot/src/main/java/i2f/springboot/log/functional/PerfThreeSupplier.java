package i2f.springboot.log.functional;

/**
 * 提供三参数的泛型支持
 *
 * @param <T>
 * @param <V1>
 * @param <V2>
 * @param <V3>
 */
@FunctionalInterface
public interface PerfThreeSupplier<T, V1, V2, V3> {
    T get(V1 v1, V2 v2, V3 v3);
}
