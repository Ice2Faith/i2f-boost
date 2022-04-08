package i2f.core.pkg.filter;


import i2f.core.annotations.remark.Author;
import i2f.core.pkg.data.ClassMetaData;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Author("i2f")
public interface IClassFilter {
    boolean save(ClassMetaData classMetaData);
}
