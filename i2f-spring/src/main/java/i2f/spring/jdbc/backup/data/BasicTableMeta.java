package i2f.spring.jdbc.backup.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ltb
 * @date 2022/10/4 9:28
 * @desc
 */
@Data
@NoArgsConstructor
public class BasicTableMeta {
    public String table;
    public List<ColumnMappingMeta> mapping;

}
