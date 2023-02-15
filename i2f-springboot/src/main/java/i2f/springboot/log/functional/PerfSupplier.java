package i2f.springboot.log.functional;

/**
 * 提供多参数
 *
 * @param <T>
 */
@FunctionalInterface
public interface PerfSupplier<T> {
    T get(Object... args);
}
