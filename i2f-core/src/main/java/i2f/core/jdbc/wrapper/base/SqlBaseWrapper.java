package i2f.core.jdbc.wrapper.base;

import i2f.core.annotations.remark.Author;
import lombok.Data;

@Author("i2f")
@Data
public class SqlBaseWrapper extends SqlBase{
    public String tableName;
}
