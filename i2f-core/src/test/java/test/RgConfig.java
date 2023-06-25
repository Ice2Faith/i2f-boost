package test;

import i2f.core.database.db.annotations.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@DbComment("乡村治理维表配置--配置、字典表：负责记录各种类型的字典信息或者配置信息，按组group_key分类之后按类型type_key分类")
@DbCatalog("car_loc_db")
@DbSchema("")
@DbName("rg_config")
@DbType("null")
@Data
@NoArgsConstructor
public class RgConfig implements Serializable {
    private static final long serialVersionUID = -1L;

    @DbComment("")
    @DbName("config_id")
    @DbType(value="BIGINT",precision=19)
    @DbNullable(false)
    @DbAutoIncrement
    @DbPrimaryKey(key="PRIMARY")
    @DbUnique(key="PRIMARY")
    @DbIndex(key="PRIMARY",type = "Other")
    private Long configId;

    @DbComment("创建时间")
    @DbName("create_time")
    @DbType(value="DATETIME",precision=19)
    @DbDefault("CURRENT_TIMESTAMP")
    private Timestamp createTime;

    @DbComment("创建人")
    @DbName("create_user")
    @DbType(value="VARCHAR",precision=50)
    private String createUser;

    @DbComment("配置项描述")
    @DbName("entry_desc")
    @DbType(value="VARCHAR",precision=2048)
    private String entryDesc;

    @DbComment("配置项编码")
    @DbName("entry_id")
    @DbType(value="VARCHAR",precision=50)
    @DbNullable(false)
    @DbUnique(key="unq_config_key")
    @DbIndex(key="unq_config_key",type = "Other")
    private String entryId;

    @DbComment("配置项键，键应该和编码具有相同作用")
    @DbName("entry_key")
    @DbType(value="VARCHAR",precision=300)
    private String entryKey;

    @DbComment("配置项名称")
    @DbName("entry_name")
    @DbType(value="VARCHAR",precision=2048)
    private String entryName;

    @DbComment("配置项排序")
    @DbName("entry_order")
    @DbType(value="BIGINT",precision=19)
    private Long entryOrder;

    @DbComment("分组键")
    @DbName("group_key")
    @DbType(value="VARCHAR",precision=300)
    @DbUnique(key="unq_config_key")
    @DbIndex(key="idx_group_key",using = "hash")
    private String groupKey;

    @DbComment("分组名称")
    @DbName("group_name")
    @DbType(value="VARCHAR",precision=300)
    private String groupName;

    @DbComment("失效时间")
    @DbName("invalid_time")
    @DbType(value="DATETIME",precision=19)
    @DbDefault("2999-12-31 00:00:00")
    private Timestamp invalidTime;

    @DbComment("层级：针对某些具有层级关系的配置或字典，提供一个层级")
    @DbName("level")
    @DbType(value="INT",precision=10)
    private Integer level;

    @DbComment("更新时间")
    @DbName("modify_time")
    @DbType(value="DATETIME",precision=19)
    @DbDefault("CURRENT_TIMESTAMP")
    private Timestamp modifyTime;

    @DbComment("更新时间")
    @DbName("modify_user")
    @DbType(value="VARCHAR",precision=50)
    private String modifyUser;

    @DbComment("父配置ID，参见：epc_config.entry_id")
    @DbName("parent_entry_id")
    @DbType(value="BIGINT",precision=19)
    private Long parentEntryId;

    @DbComment("状态： 0 禁用，1 启用")
    @DbName("status")
    @DbType(value="CHAR",precision=1)
    @DbDefault("1")
    @DbIndex(key="idx_status",type = "Other")
    private String status;

    @DbComment("分类键")
    @DbName("type_key")
    @DbType(value="VARCHAR",precision=300)
    @DbUnique(key="unq_config_key")
    @DbIndex(key="idx_type_key",using = "hash")
    private String typeKey;

    @DbComment("分类名称")
    @DbName("type_name")
    @DbType(value="VARCHAR",precision=300)
    private String typeName;

    @DbComment("生效时间")
    @DbName("valid_time")
    @DbType(value="DATETIME",precision=19)
    @DbDefault("1940-01-01 00:00:00")
    private Timestamp validTime;

}
