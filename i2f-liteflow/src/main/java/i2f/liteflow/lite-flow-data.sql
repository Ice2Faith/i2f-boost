INSERT INTO lite_flow_process (id,pkey,version,active,folder,name,remark,graph_img,graph_file,create_time,create_by,update_time,update_by) VALUES
	 (1,'leave',0,1,'hr','请假流程','请假流程',NULL,NULL,'2023-06-01 00:00:00','admin',NULL,NULL);

INSERT INTO lite_flow_node (pkey,process_id,name,node_type,handle_type,handle_by) VALUES
	 ('begin',1,'开始',0,0,'-'),
	 ('mgt2',1,'二级主管审批',2,0,'mgt2'),
	 ('mgt',1,'经理审批',2,0,'mgt'),
	 ('hr',1,'人事归档',1,1,'hr'),
	 ('fn',1,'财务归档',1,0,'fn');

INSERT INTO lite_flow_dag (id,process_id,begin_node_pkey,end_node_pkey,flow_condition) VALUES
	 (1,1,'begin','mgt2','true'),
	 (2,1,'mgt2','mgt','true'),
	 (3,1,'mgt','hr','true'),
	 (4,1,'hr','fn','true');