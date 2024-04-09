----------------------------------------------------
tl.test.Test.testSelect:566
Bql{
sql     = select su.*,d1.dict_text as status_desc,( select name from sys_role r where r.id=su.role_id ) as role_name,( case when su.del_flag=1 then ? when su.del_flag=0 then ? end ) as del_flag_desc from sys_user su left join ( select dict_value,dict_text from sys_dict where dict_type = ? ) d1 on su.status=d1.dict_value where ( su.username=? ) and ( su.age>=? ) and su.age = ? and su.username like ? and su.age != ? and su.age <> ? and instr(su.nickname,?) > 0 and ( su.age < ? or su.age >= ? or ( su.status is not null and su.del_flag is null ) ) and su.grade in ( ?,?,?,?,? ) and  exists ( select p.dict_value,p.dict_text from sys_dict p where dict_type = ? and p.dict_type = su.status )
, args  = [删除, 正常, user_status, zhang, 24, 24, wang, 12, 14, admin, 35, 18, 6, 4, 8, 9, 10, user_status]
}
Bql{
sql     = select su.*,d1.dict_text as status_desc,( select name from sys_role r where r.id=su.role_id ) as role_name,( case when su.del_flag=1 then ? when su.del_flag=0 then ? end ) as del_flag_desc from sys_user su left join ( select dict_value,dict_text from sys_dict where dict_type = ? ) d1 on su.status=d1.dict_value where ( su.username=? ) and ( su.age>=? ) and su.age = ? and su.username like ? and su.age != ? and su.age <> ? and instr(su.nickname,?) > 0 and ( su.age < ? or su.age >= ? or ( su.status is not null and su.del_flag is null ) ) and su.grade in ( ?,?,?,?,? ) and  exists ( select dict_value,dict_text from sys_dict p where dict_type = ? and dict_type = su.status )
, args  = [删除, 正常, user_status, zhang, 24, 24, wang, 12, 14, admin, 35, 18, 6, 4, 8, 9, 10, user_status]
}
----------------------------------------------------
tl.test.Test.testInsert:502
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1]
}
----------------------------------------------------
tl.test.Test.testBatchInsert:416
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() ),( ?,?,?,?,now() ),( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1, li, li, 25, 1, wang, wang, 26, 1]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() ),( ?,?,?,?,now() ),( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1, li, li, 25, 1, wang, wang, 26, 1]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) values ( ?,?,?,?,now() ),( ?,?,?,?,now() ),( ?,?,?,?,now() )
, args  = [zhang, zhang, 24, 1, li, li, 25, 1, wang, wang, 26, 1]
}
----------------------------------------------------
tl.test.Test.testBatchSelectInsert:311
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual
, args  = [zhang, zhang, zhang0, li, li, li1, wang, wang, wang2]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual
, args  = [zhang, zhang, zhang0, li, li, li1, wang, wang, wang2]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual
, args  = [zhang, zhang, zhang0, li, li, li1, wang, wang, wang2]
}
Bql{
sql     = insert into sys_user ( username,nickname,age,status,create_time ) select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual union all select ? as username,? as nickname,? as age,1 as status,now() as create_time from dual
, args  = [zhang, zhang, zhang0, li, li, li1, wang, wang, wang2]
}
----------------------------------------------------
tl.test.Test.testUpdate:273
Bql{
sql     = update sys_user set username = ?,nickname = ?,del_flag = ?,role_id = null where id = ? and status = ?
, args  = [zhang, zhang, 0, 1, 1]
}
Bql{
sql     = update sys_user set username = ?,nickname = ?,del_flag = ?,role_id = null where id = ? and status = ?
, args  = [zhang, zhang, 0, 1, 1]
}
----------------------------------------------------
tl.test.Test.testDelete:241
Bql{
sql     = delete from sys_user where del_flag = ? and ( create_time < ? or update_time < ? )
, args  = [1, 10, 10]
}
Bql{
sql     = delete from sys_user where del_flag = ? and ( create_time < ? or update_time < ? )
, args  = [1, 10, 10]
}
----------------------------------------------------
tl.test.Test.testCreateTable:203
Bql{
sql     = create table sys_user ( id bigint primary key comment 'ID',role_id bigint foreign key references sys_role(id) comment '角色ID',username varchar(300) not null comment '用户名',password varchar(300) not null comment '密码',status tinyint default 1 comment '状态：''0'' 禁用，1 正常',create_time datetime default now() comment '创建时间',update_time datetime comment '更新时间' ) comment '用户表'
, args  = []
}
Bql{
sql     = create table sys_user ( id bigint primary key comment 'ID',role_id bigint foreign key references sys_role(id) comment '角色ID',username varchar(300) not null comment '用户名',password varchar(300) not null comment '密码',status tinyint default 1 comment '状态：''0'' 禁用，1 正常',create_time datetime default now() comment '创建时间',update_time datetime comment '更新时间' ) comment '用户表'
, args  = []
}
----------------------------------------------------
tl.test.Test.testCreateIndex:181
Bql{
sql     = create unique index idx_sys_user_status_del on sys_user ( status,del_flag )
, args  = []
}
Bql{
sql     = create unique index idx_sys_user_status_del on sys_user ( status,del_flag )
, args  = []
}
----------------------------------------------------
tl.test.Test.testLambda:163
Bql{
sql     = select username,age from sys_user where username = ? and status = ? and age >= ?
, args  = [admin, 1, 18]
}
----------------------------------------------------
tl.test.Test.testBeanQuery:101
Bql{
sql     = select id,username as userName,password,role_id as roleId,nickname as nickName,age,grade,status,create_time as createTime,update_time as updateTime,del_flag as delFlag from sys_user where age = ? and status = ? and del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = select su.id,su.username as userName,su.password,su.role_id as roleId,su.nickname as nickName,su.age,su.grade,su.status,su.create_time as createTime,su.update_time as updateTime,su.del_flag as delFlag from sys_user su where su.age = ? and su.status = ? and su.del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = select id,username as userName,nickname as nickName from sys_user where age = ? and status = ? and del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = select password,role_id as roleId,age,grade,status,create_time as createTime,update_time as updateTime,del_flag as delFlag from sys_user where age = ? and status = ? and del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = select su.id,su.username as userName,su.nickname as nickName from sys_user su where su.age = ? and su.status = ? and su.del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = select su.id,su.username as userName,su.password,su.role_id as roleId,su.nickname as nickName,su.age,su.grade,su.status,su.create_time as createTime,su.update_time as updateTime,su.del_flag as delFlag from sys_user su where su.age = ? and su.status = ? and su.del_flag = ? and su.update_time is null and su.create_time is not null order by su.username
, args  = [24, 1, 0]
}
----------------------------------------------------
tl.test.Test.testBeanUpdate:78
Bql{
sql     = update sys_user set age = ?,status = ?,del_flag = ? where id = ?
, args  = [24, 1, 0, 101]
}
Bql{
sql     = update sys_user set age = ?,status = ?,del_flag = ?,age = null where id = ?
, args  = [24, 1, 0, 101]
}
----------------------------------------------------
tl.test.Test.testBeanDelete:57
Bql{
sql     = delete from sys_user where age = ? and status = ? and del_flag = ?
, args  = [24, 1, 0]
}
Bql{
sql     = delete from sys_user where age = ? and status = ? and del_flag = ? and role_id is null
, args  = [24, 1, 0]
}
ok

