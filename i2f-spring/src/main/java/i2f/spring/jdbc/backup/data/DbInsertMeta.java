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
public class DbInsertMeta extends BasicTableMeta {
    public DbInsertPrepareMeta prepare;
    public Boolean unionMode;
    public Integer batchCount;
    public BasicIoMeta input;
}
