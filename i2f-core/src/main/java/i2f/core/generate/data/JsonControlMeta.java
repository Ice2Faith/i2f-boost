package i2f.core.generate.data;

import i2f.core.annotations.remark.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author ltb
 * @date 2021/10/20
 */
@Author("i2f")
@Data
@NoArgsConstructor
public class JsonControlMeta {
    public String action;
    public String routeExpression;
    public Map<String,String> parameters;
}
