package i2f.spring.jdbc.backup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/8 9:43
 * @desc
 */
@Data
@NoArgsConstructor
public class DbInsertPrepareMeta {
    public Boolean truncate;
    public Boolean backup;
    public String backupSuffix;
}
