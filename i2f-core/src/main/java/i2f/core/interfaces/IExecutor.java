package i2f.core.interfaces;

/**
 * @author ltb
 * @date 2022/10/27 10:27
 * @desc
 */
@FunctionalInterface
public interface IExecutor<E, R> {
    R exec(E elem);
}
