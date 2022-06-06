package i2f.springboot.zplugin.config.dto;

import i2f.core.convert.ITreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/06/06 15:47
 * @desc
 */
@Data
@NoArgsConstructor
public class ConfigDto implements ITreeNode<ConfigDto> {
    private String configId; //

    private String groupId; //  分组键

    private String typeId; //  分类键

    private String entryId; //  配置项编码
    private String entryKey; //  配置项键，键应该和编码具有相同作用
    private String entryName; //  配置项名称

    private String entryDesc; //  配置项描述
    private String entryTag; // 配置项附加数据

    private String status; //  状态： 0 禁用，1 启用

    private String level; //  层级：针对某些具有层级关系的配置或字典，提供一个层级
    private String parentEntryId; //  父配置ID，参见：epc_config.entry_id

    private List<ConfigDto> children;

    @Override
    public void asMyChild(ConfigDto val) {
        if(children==null){
            children=new ArrayList<>();
        }
        children.add(val);
    }

    @Override
    public boolean isMyChild(ConfigDto val) {
        return this.entryId.equals(val.getParentEntryId());
    }

    @Override
    public boolean isMyParent(ConfigDto val) {
        return val.getEntryId().equals(this.getParentEntryId());
    }
}
