package i2f.core.convert;

import i2f.core.annotations.remark.Author;

/**
 * @author ltb
 * @date 2021/9/29
 */
@Author("i2f")
public interface ITreeNode<T> {
    void asMyChild(T val);
    boolean isMyChild(T val);
    boolean isMyParent(T val);
}
