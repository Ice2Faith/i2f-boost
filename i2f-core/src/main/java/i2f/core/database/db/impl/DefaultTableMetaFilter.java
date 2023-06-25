package i2f.core.database.db.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.database.db.data.TableMeta;
import i2f.core.lang.functional.common.IFilter;

/**
 * @author ltb
 * @date 2022/3/23 19:53
 * @desc
 */
@Author("i2f")
public class DefaultTableMetaFilter implements IFilter<TableMeta> {
    @Override
    public boolean test(TableMeta val) {
        String type = val.getType();
        if (type == null) {
            return true;
        }
        type = type.toLowerCase();
        if (type.contains("system") || type.contains("view")) {
            return false;
        }
        return true;
    }
}
