package i2f.springboot.zplugin.config.vo;

import i2f.springboot.zplugin.config.data.ServiceExportBaseVo;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/06/06 15:49
 * @desc
 */
@Data
@NoArgsConstructor
public class ConfigVo extends ServiceExportBaseVo {
    private String groupId; //  分组键

    private String typeId; //  分类键

    private String entryId; //  配置项编码
    private String entryKey; //  配置项键，键应该和编码具有相同作用

    private String status; //  状态： 0 禁用，1 启用

    private String level; //  层级：针对某些具有层级关系的配置或字典，提供一个层级
    private String parentEntryId; //  父配置ID，参见：entry_id

    private String filterType; // 不设置或0：默认，1 带上entry_key,entry_desc,entry_tag，2 带上entry_key,entry_desc,entry_tag,group_id,type_id,3 带上entry_key,entry_desc,entry_tag,group_id,type_id,status,level

    private String maxLevel; // 筛选最大层级，最小层级，默认最大层级为3
    private String minLevel; // 默认最小层级不限制

}
