package i2f.core.str.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/11/3
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class RegexFindPartMeta {
    public String part; //字符串部分
    public boolean isMatch; //指示是否是匹配的项
}
