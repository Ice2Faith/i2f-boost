package i2f.generator.er;

import i2f.core.database.db.data.TableMeta;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/6/15 20:44
 * @desc
 */
@Data
public class ErContext {
    private List<ErEntity> entities;
    private List<ErAttribute> attrs;
    private List<ErLine> lines;
    public static ErContext parse(List<TableMeta> metas){
        List<ErEntity> entities=ErEntity.parse(metas);
        return parseEntity(entities);
    }
    public static ErContext parseEntity(List<ErEntity> entities){
        ErContext ret=new ErContext();
        ret.entities=new ArrayList<>();
        ret.attrs=new ArrayList<>();
        ret.lines=new ArrayList<>();
        int i=0;
        int offHei=0;
        for(ErEntity item : entities){
            item.setPosY(offHei);
            int offWid=0;
            int maxWid=0;
            List<ErAttribute> attrs=item.getAttrs();
            offHei+=item.getHeight();
            if(attrs!=null && attrs.size()>0){
                offHei+=attrs.get(0).getHeight();

                int acnt=0;
                for(ErAttribute attr : attrs){
                    attr.setPosY(offHei);
                    attr.setPosX(offWid);
                    offWid+=attr.getWidth();
                    offWid+=20;
                    if(maxWid<offWid){
                        maxWid=offWid;
                    }
                    if(acnt%20==19){
                        offWid=0;
                        offHei+=attrs.get(0).getHeight();
                        offHei+=20;
                    }
                    acnt++;
                }

                offHei+=attrs.get(0).getHeight();
                offHei+=20;
            }
            item.setPosX(maxWid/2);
            ret.entities.add(item);
            ret.attrs.addAll(item.getAttrs());
            ret.lines.addAll(item.getLines());

            offHei+=20;
        }

        return ret;
    }

}
