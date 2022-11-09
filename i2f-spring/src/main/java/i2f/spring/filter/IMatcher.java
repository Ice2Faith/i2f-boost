package i2f.spring.filter;

/**
 * @author ltb
 * @date 2022/10/27 10:28
 * @desc
 */
@FunctionalInterface
public interface IMatcher<E, P> {
    boolean match(E elem, P patten);
}
