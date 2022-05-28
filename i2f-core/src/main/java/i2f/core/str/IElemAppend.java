package i2f.core.str;

/**
 * @author ltb
 * @date 2022/5/28 17:39
 * @desc
 */
@FunctionalInterface
public interface IElemAppend<T extends Appendable,E> {
    void append(Appender<T> appender,E elem);
}
