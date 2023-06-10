
/**
 * 计划支持任务
 * 部署-管理员部署指定的流程-deploy
 * 新建-提交人发起流程-begin
 * 取消-发起人取消流程-cancel
 * 提交-处理人处理流程-submit
 * 回退-处理人驳回流程-rollback
 * 结束-处理人结束流程-finish
 * 认领-处理人从组中认领任务-claim
 * 交出-处理人交出任务到组中-surrender
 * 
 * 并行开始-任务提交后支持多个子流程并发
 * 并行结束-等待所有子流程结束才继续流程
 */
create table lite_flow_process
(
	id bigint comment '流程ID',
	
    pkey varchar(80)  comment '流程键',
    version int comment '版本',
    active int comment '是否激活：0 否，1 是',
    folder varchar(300) comment '归属目录',

    name varchar(300) comment '名称',
    remark varchar(1024) comment '备注',

    graph_img varchar(1024) comment '图-图片',
    graph_file varchar(1024) comment '图-原始文件',


    create_time datetime comment '创建时间',
    create_by varchar(300) comment '创建人',
    update_time datetime comment '更新时间',
    update_by varchar(300) comment '更新人',
    primary key(id),
    key idx_pkey_version(pkey,version),
    key idx_pkey_active(pkey,active),
    key idx_name(name),
    key idx_folder(folder)
) comment '流程表';

create table lite_flow_node
(
    pkey varchar(80) comment '节点键',

    process_id bigint comment '流程ID',

    name varchar(300) comment '节点名称',
    node_type int comment '节点类型：0 开始，1 结束，2 常规，3 并行开始，4 并行结束',

    handle_type int comment '处理类型：0 个人，1 组织',
    handle_by varchar(300) comment '处理主体：个人或组织的键',

    key idx_process_id(process_id),
    unique key unq_process_node(process_id,pkey),
    key idx_process_name(process_id,name)
) comment '流程节点表';

create table lite_flow_dag
(
	id bigint comment '有向边ID',
    process_id bigint comment '流程ID',

    begin_node_pkey varchar(80) comment '开始节点ID',
    end_node_pkey varchar(80) comment '结束节点ID',

    flow_condition varchar(1024) comment '流转条件',

    primary key(id),
    unique key unq_process_begin_end(process_id,begin_node_pkey,end_node_pkey),
    key idx_process_end_begin(process_id,end_node_pkey,begin_node_pkey)
) comment '流程有向图表';


create table lite_flow_instance
(
    id bigint comment '主键',
    process_id bigint comment '流程ID',
    business_key varchar(300) comment '业务键',
    status int comment '实例状态：0 停止，1 运行，2 结束',
    create_time datetime comment '创建时间',
    create_by varchar(300) comment '创建人',
    primary key(id),
    key idx_process_status(process_id,status)
) comment '流程实例表';

create table lite_flow_log
(
    id bigint comment '主键',
    instance_id bigint comment '实例ID',
    node_key varchar(80) comment '当前节点键',
    prev_id bigint comment '上一节点ID',
    prepared int  comment '是否已经准备完毕：0 否，1 是',
    newest int comment '是否最新节点：0 否，1 是',


    handle_time datetime comment '处理时间',
    handle_msg varchar(2048) comment '处理消息',
    handle_by varchar(300) comment '处理人',

    param1 varchar(300) comment '参数',
    param2 varchar(300) comment '参数',
    param3 varchar(300) comment '参数',
    param4 varchar(300) comment '参数',
    param5 varchar(300) comment '参数',

    primary key(id),
    key idx_instance_newest(instance_id,newest),
    key idx_prev(prev_id)
) comment '流程日志表';

create table lite_flow_params
(
    id bigint auto_increment comment '主键',
    instance_id bigint comment '实例ID',
    entry_key varchar(80) comment '参数键',
    entry_type varchar(300) comment '参数类型',
    entry_value varchar(2048) comment '参数内容',
    primary key(id),
    key idx_instance(instance_id)
) comment '流程参数表';


create table lite_flow_active
(
	id bigint comment '有向边ID',
    instance_id bigint comment '实例ID',

    begin_node_pkey varchar(80) comment '开始节点ID',
    end_node_pkey varchar(80) comment '结束节点ID',

    primary key(id),
    unique key unq_instance_begin_end(instance_id,begin_node_pkey,end_node_pkey),
    key idx_instance_end_begin(instance_id,end_node_pkey,begin_node_pkey)
) comment '流程激活表';