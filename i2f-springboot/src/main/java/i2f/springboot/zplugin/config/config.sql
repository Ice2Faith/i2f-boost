CREATE TABLE sys_config (
  config_id bigint(20) NOT NULL AUTO_INCREMENT,

  group_id varchar(300) DEFAULT NULL COMMENT '分组键',
  group_name varchar(300) DEFAULT NULL COMMENT '分组名称',

  type_id varchar(300) DEFAULT NULL COMMENT '分类键',
  type_name varchar(300) DEFAULT NULL COMMENT '分类名称',

  entry_id bigint(20) NOT NULL COMMENT '配置项编码',
  entry_key varchar(300) DEFAULT NULL COMMENT '配置项键，键应该和编码具有相同作用',
  entry_name varchar(2048) DEFAULT NULL COMMENT '配置项名称',

  entry_desc varchar(2048) DEFAULT NULL COMMENT '配置项描述',
  entry_tag varchar(2048) DEFAULT NULL COMMENT '配置项附加数据',
  entry_order bigint(20) DEFAULT NULL COMMENT '配置项排序',

  status char(1) DEFAULT '1' COMMENT '状态： 0 禁用，1 启用',

  level int(11) DEFAULT NULL COMMENT '层级：针对某些具有层级关系的配置或字典，提供一个层级',
  parent_entry_id bigint(20) DEFAULT NULL COMMENT '父配置ID，参见：entry_id',

  valid_time datetime DEFAULT '1940-01-01 00:00:00' COMMENT '生效时间',
  invalid_time datetime DEFAULT '2999-12-31 00:00:00' COMMENT '失效时间',

  create_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  create_user varchar(50) DEFAULT NULL COMMENT '创建人',
  modify_time datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  modify_user varchar(50) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (config_id),
  UNIQUE KEY unq_config_key (group_key,type_key,entry_id) USING HASH,
  KEY idx_group_key (group_key) USING HASH,
  KEY idx_type_key (type_key) USING HASH,
  KEY idx_status (status) USING HASH,
  KEY idx_parent_entry_id (parent_entry_id) USING BTREE,
  KEY idx_parent_entry_id_entry_id (parent_entry_id,entry_id) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='配置、字典表：负责记录各种类型的字典信息或者配置信息，按组group_key分类之后按类型type_key分类'
