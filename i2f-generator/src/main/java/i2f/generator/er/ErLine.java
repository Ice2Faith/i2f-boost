package i2f.generator.er;

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
public class ErLine {
    private String id;
    private String formId;
    private String toId;
    private String text;
    public static ErLine parse(ErEntity entity,ErAttribute attr){
        ErLine ret=new ErLine();
        ret.id= CodeUtil.makeUUID();
        ret.formId= entity.getId();
        ret.toId= attr.getId();
        ret.text="";

        return ret;
    }
    public static void parse(ErEntity entity){
        List<ErLine> lines=new ArrayList<>();
        for(ErAttribute item : entity.getAttrs()){
            ErLine line=parse(entity,item);
            lines.add(line);
        }

        entity.setLines(lines);
    }
}
