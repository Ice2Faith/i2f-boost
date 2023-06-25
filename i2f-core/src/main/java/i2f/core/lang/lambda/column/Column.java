package i2f.core.lang.lambda.column;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/9/1 10:29
 * @desc
 */
@Data
@NoArgsConstructor
public class Column {
    public Column(Object prefix, Object column, Object alias) {
        this.prefix = prefix;
        this.column = column;
        this.alias = alias;
    }

    public Object prefix;
    public Object column;
    public Object alias;
}
