CREATE TABLE sys_log (
  id bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  src_system varchar(50)  COMMENT '来源系统',
  src_module varchar(50)  COMMENT '来源模块',
  src_label varchar(50)  COMMENT '来源标签',
  log_location varchar(500)  COMMENT '日志位置（className）',
  log_type int  COMMENT '日志类型：0 登录日志，1 登出日志，2 注册日志，3 注销日志，4 接口日志，5 输出日志，6 服务器状态，7 服务异常，8 调用日志，9 回调日志',
  log_content text COMMENT '日志内容',
  log_level int  COMMENT '日志级别：0 ERROR，1 WARN，2 INFO，3 DEBUG，4 TRACE',
  operate_type int  COMMENT '操作类型：0 查询，1 新增，2 修改，3 删除，4 申请，5 审批，6 导入，7 导出',
  log_key varchar(300)  COMMENT '日志键',
  log_val varchar(300)  COMMENT '日志值',
  trace_id varchar(64)  COMMENT '跟踪ID',
  trace_level int  COMMENT '跟踪层次',
  user_id varchar(64)  COMMENT '操作用户账号',
  user_name varchar(256)  COMMENT '操作用户名称',
  client_ip varchar(256)  COMMENT '客户端IP',
  java_method varchar(1024)  COMMENT '请求java方法',
  except_type int  COMMENT '异常分类，0 Exception,1 RuntimeException,2 Error,3 Throwable,4 SQLException',
  except_class varchar(500)  COMMENT '异常类',
  except_msg varchar(500)  COMMENT '异常信息',
  except_stack text COMMENT '异常堆栈',
  request_url varchar(2048)  COMMENT '请求路径',
  request_param text COMMENT '请求参数',
  request_type varchar(10)  COMMENT '请求类型',
  cost_time bigint  COMMENT '耗时，毫秒',
  create_time datetime  COMMENT '创建时间',
  PRIMARY KEY (id),
  KEY idx_system_module_label (src_system,src_module,src_label),
  KEY idx_log_type (log_type),
  KEY idx_log_level (log_level),
  KEY idx_operate_type (operate_type),
  KEY idx_user_id (user_id)
) ENGINE=MyISAM COMMENT='系统日志表';