package i2f.core.border;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:01
 * @desc
 */
public interface BorderInDecider<E, R> {
    boolean isInRange(E elem, R range);
}
