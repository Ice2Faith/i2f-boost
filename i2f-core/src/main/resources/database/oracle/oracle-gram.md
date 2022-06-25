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
- 转换为日期时间
```sql
to_date(#{createTime,jdbcType=VARCHAR},'yyyy-MM-dd hh24:mi:ss')
```
- 时间加减
- {date} [+|-] INTERVAL {value} [ YEAR | MONTH | DAY | HOUR | MINUTE | SECODE ]
```sql
select 
    sysdate + INTERVAL '1' day
from dual;
select 
    sysdate - INTERVAL '2' hour
from dual;
```
- group concat
```sql
select a.qu_id , count(*) repo_cnt,
        LISTAGG( b.title, ',') WITHIN GROUP(ORDER BY b.title asc) AS repo_strs
        from exam_qu_repo_ref a
        left join exam_qu_repo b on b.id =a.repo_id
        group by a.qu_id
```
- random order
```sql
ORDER BY dbms_random.value()
```
- linenumber
    - 没有对应的函数或者关键字
    - 使用自定义变量方式实现
```sql
select (@rownum:=@rownum+1) rownum,a.*
from sys_user a,
(select @rownum:=0) b
```
- page
```sql
select *
from (
    select tmp.*,rownum row_id
    from (
            select a.*
            from sys_user a
    ) tmp
    where rownum<= 20
) tmp
where row_id > 0
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
- 添加注释
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
on tb_user(login_name,status);
```
- 修改表字段类型
    - 原理就是，使用中间列做转换
```sql
alter table tb_user
rename column tar_col to tmp_col;

alter table tb_user
add tar_col Long;

update tb_user
set tar_col=tmp_col
where 1=1;

alter table tb_user
drop column tmp_col;
```
- 查看表的建表语句
```sql
select dbms_metadata.get_ddl('tb_user','a') 
from dual;
```
- 重命名列
```sql
alter table tb_user
rename column col_age to col_age_new;
```
- 添加列
```sql
alter table tb_user
add col_fav varchar2(30);
```
- 删除列
```sql
alter table tb_user
drop column col_fav;
```
