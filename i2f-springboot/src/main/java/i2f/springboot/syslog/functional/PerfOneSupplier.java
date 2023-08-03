package i2f.springboot.syslog.functional;

/**
 * 提供单参数的泛型支持
 *
 * @param <T>
 * @param <V>
 */
@FunctionalInterface
public interface PerfOneSupplier<T, V> {
    T get(V val);
}
