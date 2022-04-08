package i2f.core.convert;

import java.util.List;

/**
 * @author ltb
 * @date 2022/3/27 22:30
 * @desc
 */
public interface INextLevelDataProvider<T> {
    List<T> getNextLevel(List<T> list);
}
