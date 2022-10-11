package i2f.spring.jdbc.backup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/4 9:29
 * @desc
 */
@Data
@NoArgsConstructor
public class ColumnMappingMeta {
    public String column;
    public String property;
}
