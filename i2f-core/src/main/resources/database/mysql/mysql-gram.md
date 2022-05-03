# MySQL 语法集锦
---
## 日期
- 当前时间
```sql
select 
    now()
from dual;
```
- 格式化日期时间
```sql
select 
    date_format(now(),'%Y-%m-%d %H:%i:%s')
from dual;
```
- 时间加减
```
select 
    date_sub(now(),interval 3 day)
from dual;

select 
    date_add(now(),interval 3 day)
from dual;
```

---
## DDL
- 创建表
```sql
create table tb_user
(
    user_id bigint primary key auto_increment comment '用户ID',
    login_name varchar(50) unique not null comment '登录名',
    password varchar(50) comment '密码',
    status char(1) default 1,
    create_time datetime default now()
) comment '用户表';
```
- 创建索引
```sql
create index idx_login_name_status
on tb_user(login_name,status)
using btree;
```
