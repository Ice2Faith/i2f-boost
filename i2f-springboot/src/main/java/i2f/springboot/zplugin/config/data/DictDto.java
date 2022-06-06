package i2f.springboot.zplugin.config.data;

import i2f.core.convert.ITreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/06/06 16:12
 * @desc
 */
@Data
@NoArgsConstructor
public class DictDto implements ITreeNode<DictDto> {
    private String entryId;
    private String entryName;
    private String entryDesc;

    private String parentEntryId;
    private String level;
    private List<DictDto> children;

    @Override
    public void asMyChild(DictDto val) {
        if(children==null){
            children=new ArrayList<>();
        }
        children.add(val);
    }

    @Override
    public boolean isMyChild(DictDto val) {
        return this.entryId.equals(val.getParentEntryId());
    }

    @Override
    public boolean isMyParent(DictDto val) {
        return val.getEntryId().equals(this.getParentEntryId());
    }

}
