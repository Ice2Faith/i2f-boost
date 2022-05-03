package com.i2f.demo.modules.mybatis.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ltb
 * @date 2022/2/17 15:47
 * @desc
 */
@Data
@NoArgsConstructor
public class ConfigDomain {
    private String configId; //

    private String groupKey; //  分组键
    private String groupName; //  分组名称

    private String typeKey; //  分类键
    private String typeName; //  分类名称

    private String entry_id;
    private String entryId; //  配置项编码
    private String entryKey; //  配置项键，键应该和编码具有相同作用
    private String entryName; //  配置项名称
    private String entryDesc; //  配置项描述

    private String entryOrder; //  配置项排序

    private String status; //  状态： 0 禁用，1 启用

    private String level; //  层级：针对某些具有层级关系的配置或字典，提供一个层级
    private String parentEntryId; //  父配置ID，参见：epc_config.entry_id

    private String validTime; //  生效时间
    private String invalidTime; //  失效时间

    private String createTime; //  创建时间
    private String createUser; //  创建人
    private String modifyTime; //  更新时间
    private String modifyUser; //  更新时间

}
