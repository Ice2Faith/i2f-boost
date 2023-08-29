# SQL 注入

## 简介
- SQL注入是利用SQL的语法特征
- 以及程序开发过程中的不严谨性质来进行非法的访问数据库的过程
- 主要的目标就是，获取系统用户列表，获取用户密码，获取数据库用户，更改数据库等

## 避免
- SQL注入的避免
- 使用预处理语句，比如 prepared-statement
- 使用SQL占位符，而不是直接拼接
- 正确使用ORM框架
- 使用SQL注入防火墙
- 正确验证数据格式

## 案例
- 条件部分注入
```sql
-- 字符型：'or 1=1 --
SELECT * FROM sys_user
WHERE username ='admin 'or 1=1 --'

-- 字符型：'or 1=1 OR ''='
SELECT * FROM sys_user
WHERE username ='admin'or 1=1 OR ''=''

-- 数值型：
SELECT * FROM sys_user
WHERE user_id =11020317 OR 1=1
```
- 在这个案例中
- 针对字符型的注入，可以将参数进行批量替换，从而避免
- 也就是对单引号进行转义
```java
String param="admin 'or 1=1 --";
param=param.replaceAll("'","''");
最终得到结果：
admin ''or 1=1 --
```
- 而对于数值型，则需要使用正则进行匹配判断
- 是比较麻烦的
```java
/^(+|-)?\d+(\.\d+)?$/
```
- 但是，如果不考虑数据库的类型隐式转换
- 则可以全部变为字符串处理即可
