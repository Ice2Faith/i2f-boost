package i2f.core.border;

/**
 * @author Ice2Faith
 * @date 2023/2/6 9:05
 * @desc
 */
public interface BorderLimitDecider<E> {
    boolean isLimit(E elem1, E elem2);
}
