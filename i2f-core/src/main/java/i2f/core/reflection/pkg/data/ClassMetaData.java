package i2f.core.reflection.pkg.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/9/23
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class ClassMetaData {
    private String className;
    private Class clazz;
    private String location;
}
