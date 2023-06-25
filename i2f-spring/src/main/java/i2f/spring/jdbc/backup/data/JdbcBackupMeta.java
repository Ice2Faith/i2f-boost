package i2f.spring.jdbc.backup.data;

import i2f.core.database.jdbc.core.JdbcMetaAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/10/4 9:39
 * @desc
 */
@Data
@NoArgsConstructor
public class JdbcBackupMeta {
    public JdbcMetaAdapter datasource;
    public DbSelectMeta select;
    public DbInsertMeta insert;
}
