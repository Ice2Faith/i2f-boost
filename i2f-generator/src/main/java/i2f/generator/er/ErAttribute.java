package i2f.generator.er;

import i2f.core.database.db.data.TableColumnMeta;
import i2f.core.digest.CodeUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 20:38
 * @desc
 */
@Data
public class ErAttribute {
    private String id;
    private int posX;
    private int posY;
    private int width;
    private int height;
    private String text;
    public static ErAttribute parse(TableColumnMeta meta){
        ErAttribute ret=new ErAttribute();
        ret.id= CodeUtil.makeUUID();
        ret.width=100;
        ret.height=60;
        ret.text=meta.getRemark();
        return ret;
    }
    public static List<ErAttribute> parse(List<TableColumnMeta> metas){
        List<ErAttribute> ret=new ArrayList<>();
        for(TableColumnMeta item : metas){
            ErAttribute attr=parse(item);
            ret.add(attr);
        }
        return ret;
    }
}
