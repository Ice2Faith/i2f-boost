# Oracle 语法集锦
---
## 日期
- 当前时间
```sql
select 
    sysdate
from dual;
```
- 格式化日期时间
```sql
select 
    to_char(sysdate,'yyyy-MM-dd hh24:mi:ss')
from dual;
```

---
## DDL
- 创建表
```sql
create table tb_user
(
    user_id bigint primary key ,
    login_name varchar(50) unique not null ,
    password varchar(50) ,
    status char(1) default 1,
    create_time date default sysdate
) ;
```
- 添加备注
```sql
comment on table tb_user 
is '用户表';

comment on column tb_user.login_name
is '登录名';
```
- 创建序列
```sql
create sequence SEQ_TB_USER_USER_ID
    increment by 1
    start with 1
    maxvalue 9999999999999;
```
- 使用序列
```sql
select 
    SEQ_TB_USER_USER_ID.nextval 
from dual
```
- 创建索引
```sql
create index idx_login_name_status
on tb_user(login_name,status)
using btree;
```

