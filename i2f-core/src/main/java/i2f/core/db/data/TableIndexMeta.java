package i2f.core.db.data;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Ice2Faith
 * @date 2023/5/13 23:04
 * @desc
 */
@Data
@NoArgsConstructor
public class TableIndexMeta {
    // primary,unique,index
    private String type;
    private String name;
    private List<String> columns;
}
