package i2f.core.dict.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ice2Faith
 * @date 2023/2/21 16:02
 * @desc
 */
@Data
@NoArgsConstructor
public class DictItem {
    protected String code;
    protected String text;
    protected String desc;
}
