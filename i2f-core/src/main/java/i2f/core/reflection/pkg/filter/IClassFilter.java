package i2f.core.reflection.pkg.filter;


import i2f.core.annotations.remark.Author;
import i2f.core.reflection.pkg.data.ClassMetaData;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Author("i2f")
public interface IClassFilter {
    boolean save(ClassMetaData classMetaData);
}
