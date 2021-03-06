package i2f.core.db.impl;

import i2f.core.annotations.remark.Author;
import i2f.core.db.data.TableMeta;
import i2f.core.interfaces.IFilter;

/**
 * @author ltb
 * @date 2022/3/23 19:53
 * @desc
 */
@Author("i2f")
public class DefaultTableMetaFilter implements IFilter<TableMeta> {
    @Override
    public boolean choice(TableMeta val) {
        String type=val.getType();
        if(type==null){
            return true;
        }
        type=type.toLowerCase();
        if(type.contains("system") || type.contains("view")){
            return false;
        }
        return true;
    }
}
