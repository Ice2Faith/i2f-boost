package i2f.core.type.str.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2021/10/18
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class RegexMatchItem {
    public String srcStr;
    public String regexStr;
    public String matchStr;
    public int idxStart;
    public int idxEnd;
}
