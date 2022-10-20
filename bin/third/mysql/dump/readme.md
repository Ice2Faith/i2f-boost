# 关键表备份
---
- 借助MySQL提供的 mysqldump+crontable 进行定期的关键表备份

---
## 使用方式
- 解压本压缩包
- 你将会得到如下文件树
```shell script
mysql8   # 默认是MYSQL8的执行dump文件
    mysqldump   # linux版本
    mysqldump.exe # windows版本
backup.bat # windows版本的备份脚本
backup.sh # linux版本的备份脚本
```
- windows下执行
```shell script
backup.bat [主机] [用户名] [密码] [数据库] [表名] [保存路径]
```
```shell script
backup.bat localhost root 123456 test_db sys_user E:\bak
```
- linux下执行
```shell script
./backup.sh [主机] [用户名] [密码] [数据库] [表名] [保存路径]
```
```shell script
./backup.sh localhost root 123456 test_db sys_user E:\bak
```

## 批量执行模板
- 一般来说，都不是单表备份
- 因此，提供一个批量备份的模板
- 根据模板编写自己的备份脚本即可
```shell script
batch.bat
batch.sh
```

---
## mysqldump 使用简介
- 备份数据库
```shell script
mysqldump [-u 用户名] [-p 密码] 数据库名 [... 表名] > 文件名
```
- 其中[]包含的参数为可选参数
- 使用示例
```shell script
mysqldump -u root -p 123456 test_db sys_user sys_role > user_role.sql
```
- 需要注意的点
- mysqldump 使用的时候，可能会导致锁表
- 因此常常结合其他几个参数一起使用
- 导出数据结构：-d
```shell script
-d 仅导出结构，如果只有数据库，则导出数据库的结构，如果指定了表，则导出表结构
```
```shell script
mysqldump [-u 用户名] [-p 密码] -d 数据库名 [... 表名] > 文件名
```
- 备份数据启用事务，不锁表 --single-transaction
```shell script
--single-transaction 使用事务，不锁表，前提是表的引擎支持事务，这样能尽可能的避免备份时锁表，导致应用卡死
```
```shell script
mysqldump [-u 用户名] [-p 密码] --single-transaction 数据库名 [... 表名] > 文件名
```
- 取数据的时候，只取一行，不一次性直接到内存中 --quick
```shell script
--quick 一次只取一行数据，不一次性直接到内存中
```
```shell script
mysqldump [-u 用户名] [-p 密码] --quick 数据库名 [... 表名] > 文件名
```
- 一般情况下推荐的命令
```shell script
mysqldump [-u 用户名] [-p 密码] --single-transaction --quick 数据库名 [... 表名] > 文件名
```

---
- 恢复数据库
```shell script
mysqldump [-u 用户名] [-p 密码] 数据库名 < 文件名
```
- 从使用上来说，恢复与备份的命令基本一致，区别在于定向符方向

- 另外一种方式，是使用mysql直接运行SQL脚本的方式
```shell script
# 进入MySQL终端
mysql -u root -p 
# 输入密码
# 切换数据库
use test_db
# 执行SQL脚本
source /home/sys_user.sql
```
