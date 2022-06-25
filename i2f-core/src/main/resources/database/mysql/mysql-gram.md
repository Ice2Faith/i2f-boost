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
- [ MICROSECOND | SECOND | MINUTE | HOUR | DAY | WEEK | MONTH | QUARTER | YEAR | ...]
```sql
select 
    date_sub(now(),interval 3 day)
from dual;

select 
    date_add(now(),interval 3 day)
from dual;
```
- group concat
```sql
select a.qu_id , count(*) repo_cnt,
         group_concat(distinct b.title order by b.title asc SEPARATOR ',') repo_strs
        from exam_qu_repo_ref a
        left join exam_qu_repo b on b.id =a.repo_id
        group by a.qu_id
```
- random order
```sql
order by rand()
```
- linenumber
```sql
selet rownum,a.*
from sys_user a
```
- page
```sql
select a.*
from sys_user a
limit 0,20
```
- 查询结果到新表
```sql
create table tb_result
as
select id,age,name 
from tb_user
where state!=0;
```
- 查询结果插入表
```sql
insert into tb_history
(id,age,name)
select id,age,name 
from tb_user
where state!=0;
```
- 批量插入
```sql
insert into tb_user
(age,name)
values
(12,'zhang'),
(14,'wang');
```
- 通用的批量插入
```sql
insert into tb_user
(age,name)
select 12,'zhang' from dual
union all
select 14,'wang' from dual;
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
-- 不存在则建立
create table if not exists tb_user
(
    id bigint primary key
);
```
- 创建索引
```sql
create index idx_login_name_status
on tb_user(login_name,status)
using btree;
```
- 查看表的建表语句
```sql
show create table tb_user;
```
- 查询所有数据库
```bash
show databases;
```
- 创建数据库
```sql
-- 直接建立
create database db_test;
-- 不存在则建立
create database if not exists db_test;
-- 同时指定字符集
create database db_test character set 'utf-8';
```
- 删除数据库
```sql
-- 直接删除
drop database db_test;
-- 存在则删除
drop database if exists db_test;
```
- 修改数据库字符集
```sql
alter database db_test
character set 'utf-8';
```
- 使用数据库
```sql
use db_test;
```
- 查看当前使用的数据库
```sql
select database();
```
- 查看所有表
```bash
show tables;
```
- 查询表结构
```bash
desc tb_user;
```
- 删除表
```sql
drop table tb_user;
drop table if exists tb_user;
```
- 修改表名
```sql
alter table tb_user
rename to tb_user_new;
```
- 添加列
```sql
alter table tb_user
add col_fav varchar(50);
```
- 修改类型
```sql
alter table tb_user
modify col_fav varchar(300);
```
- 修改列名和数据类型
```sql
alter table tb_user
change col_fav col_fav_new varchar(500);
```
- 删除列
```sql
alter table tb_user
drop col_fav;
```
- 创建外键
```sql
alter table tb_user
add constraint fk_role_id foreign key role_id references tb_role(id);
```
- 删除外键
```sql
alter table tb_user
drop foreign key fk_role_id;
```
- 清空表
```sql
delete from tb_user;
-- 效率更高
truncate table tb_user;
```
- 创建唯一索引
```sql
create unique index unq_user_name
on tb_user(user_name);
```
- 创建一般索引
```sql
create index idx_create_time
on tb_user(create_time);
```
- 创建组合索引
```sql
create index idx_state_organ
on tb_user(state,organ);
```
- 查看索引
```sql
show index from tb_user;
```
- 删除索引
```sql
drop index idx_create_time
on tb_user;
```
- 修改表注释
```sql
alter table tb_user
comment 'new comment';
```
- 修改列注释
```sql
alter table tb_user
modify column organ int comment 'new comment';
```
- 创建一个镜像表
```sql
-- 携带表数据
create table tb_user_bak
as
select * from tb_user;
-- 可以通过where条件，限制不携带数据
-- 从而创建一张表结构一样的空表
create table tb_user_bak
as
select * from tb_user
where 1=2;
```
