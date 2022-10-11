package i2f.spring.jdbc.backup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/4 9:32
 * @desc
 */
@Data
@NoArgsConstructor
public class DbSelectMeta {
    public String sql;
    public Boolean pageable;
    public Integer pageSize;
    public BasicIoMeta output;
}
