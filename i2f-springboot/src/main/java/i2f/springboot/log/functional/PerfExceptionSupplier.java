package i2f.springboot.log.functional;

/**
 * 提供异常情况下的多参数
 *
 * @param <T>
 * @param <E>
 */
@FunctionalInterface
public interface PerfExceptionSupplier<T, E extends Throwable> {
    T get(E e, Object... args);
}
