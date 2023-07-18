# mysql8 环境安装

## 简介
- mysql是中小型项目中常用的数据库
- 目前mysql8已经成为主流版本

## 依赖环境
- 无

## 安装
- 下载yum
```shell script
curl https://repo.mysql.com//mysql80-community-release-el7-3.noarch.rpm > centos7.mysql.rpm
```
- 安装rpm
```shell script
yum install centos7.mysql.rpm -y
```
- 禁用已有mysql
```shell script
yum module disable mysql

确认禁用,输入
y
```
- 升级GPTkey
```shell script
rpm --import https://repo.mysql.com/RPM-GPG-KEY-mysql-2022
```
- 安装mysql
```shell script
yum install mysql-community-server -y
```
- 启动mysql
```shell script
systemctl start mysqld
```
- 设置开机启动
```shell script
systemctl enable mysqld
```
- 查看初始密码
```shell script
grep "password" /var/log/mysqld.log
```
- 使用初始密码登录
```shell script
mysql -uroot -p
```
- 修改默认密码
    - 下面密码设置为： xxx123456
```shell script
set global validate_password.policy=0;  #修改密码安全策略为低（只校验密码长度，至少8位）。
set global validate_password.length=1;
ALTER USER 'root'@'localhost' IDENTIFIED BY 'xxx123456';
```
- 授予远程权限
```shell script
create user root@'%' identified by 'xxx123456';
grant all privileges on *.* to root@'%' with grant option;
```
- 更改加密方式，防止navicat 出现 2059 错误
```shell script
# 使用系统库
use mysql;
# 更改加密方式
ALTER USER 'root'@'%' IDENTIFIED BY 'xxx123456' PASSWORD EXPIRE NEVER;
# 更改用户密码
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'xxx123456';
# 刷新权限
flush privileges;
```
- 退出mysql命令行
```shell script
exit
```
- 更改配置，防止远程连接出现 2013 错误
```shell script
vim /etc/my.cnf
```
- 修改[mysqld]节点下的内容
```shell script
[mysqld]
# 远程数据库连接错误2013-Lost Connection...
skip-name-resolve
# bind-address = 127.0.0.1
```
- 保存并退出
- 重启mysql
```shell script
systemctl stop mysqld
systemctl start mysqld
```
- 验证
```shell script
sudo netstat -tap | grep mysql
```


## 卸载
- 关闭服务
```shell script
systemctl stop mysqld
```
- 移除rpm
```shell script
rpm -qa|grep -i mysql
rpm -ev -mysql-community-libs-8.0.23-1.el7.x86_64 --nodeps
rpm -ev mysql80-community-release-el7-3.noarch --nodeps
rpm -ev mysql-community-common-8.0.23-1.el7.x86_64 --nodeps
rpm -ev mysql-community-client-8.0.23-1.el7.x86_64 --nodeps
rpm -ev mysql-community-client-plugins-8.0.23-1.el7.x86_64 --nodeps
rpm -ev mysql-community-server-8.0.23-1.el7.x86_64 --nodeps
```
- 删除文件
```shell script
rm -rf /var/lib/selinux/targeted/active/modules/100/mysql
rm -rf /var/lib/selinux/targeted/tmp/modules/100/mysql
rm -rf /var/lib/mysql
rm -rf /var/lib/mysql/mysql
rm -rf /usr/lib64/mysql
rm -rf /usr/share/bash-completion/completions/mysql
rm -rf /usr/share/selinux/targeted/default/active/modules/100/mysql
rm -rf /etc/my.cnf*
```
- 检查残留
```shell script
rpm -qa|grep -i mysql
```


## 下载包手动安装方式
- 下载
```shell script
echo https://dev.mysql.com/downloads/
wget xxx
```
- 我的pwd是这样的
```shell script
/home/env/mysql8
```
- 得到包
```shell script
mysql-8.0.30-linux-glibc2.17-x86_64-minimal.tar.xz
```
- 解包
```shell script
xz -d mysql-8.0.30-linux-glibc2.17-x86_64-minimal.tar.xz
tar -xvf mysql-8.0.30-linux-glibc2.17-x86_64-minimal.tar
```
- 重命名
```shell script
mv mysql-8.0.30-linux-glibc2.17-x86_64-minimal mysql-8.0.30
```
- 创建用户组
```shell script
groupadd mysql
useradd -g mysql mysql
```
- 创建数据存储目录
```shell script
mkdir -p /opt/env/mysql/data
mkdir -p /opt/env/mysql/logs
mkdir -p /opt/env/mysql/log
mkdir -p /opt/env/mysql/temp
```
- 更改目录所有者
```shell script
chown -R mysql:mysql mysql-8.0.30
chown -R mysql:mysql /opt/env/mysql
chmod -R a+rwx mysql-8.0.30
chmod -R a+rwx /opt/env/mysql
```
- 初始化
    - 注意，这一步最后会输出随机的密码
    - 后面要使用，记得保存
```shell script
./mysqld --user=mysql --basedir=/home/env/mysql8/mysql-8.0.30 --datadir=/opt/env/mysql8/data --tmpdir=/opt/env/mysql8/temp --log-error=/opt/env/mysql8/logs/mysql.err --lower-case-table-names=1 --initialize```
- 编辑配置文件
```shell script
vi /etc/my.cnf
```
```shell script
[mysqld]
bind-address=0.0.0.0
port=3306
user=mysql
basedir=/home/env/mysql8/mysql-8.0.30
datadir=/opt/env/mysql/data
tmpdir=/opt/env/mysql/temp
socket=/tmp/mysql.sock
log-error=/opt/env/mysql/logs/mysql.err
pid-file=/opt/env/mysql/mysql.pid
#character config
character_set_server=utf8mb4
collation-server=utf8mb4_general_ci
symbolic-links=0
explicit_defaults_for_timestamp=true

skip-name-resolve

# 开启 Binlog 并写明存放日志的位置
#log_bin = /opt/env/mysql/log/binlog

# 指定索引文件的位置
#log_bin_index = /opt/env/mysql/log/mysql-bin.index

#删除超出这个变量保留期之前的全部日志被删除
expire_logs_days = 14

# 指定一个集群内的 MySQL 服务器 ID，如果做数据库集群那么必须全局唯一，一般来说不推荐 指定 server_id >等于 1。
server_id = 1

max_connections=2000
connect_timeout=20000
max_connect_errors=500

# 指定执行器数量
innodb_buffer_pool_instances=4
```
- 更改权限
```shell script
chmod a+rw /etc/my*
```
- 添加服务到系统
```shell script
cp -a ./support-files/mysql.server /etc/init.d/mysql
chmod a+x /etc/init.d/mysql
chkconfig --add mysql
```
- 启动mysql
```shell script
systemctl start mysql
```
- 之后的初始化过程和rpm自动安装的过程一致
- 不再赘述


## 通过docker安装mysql
- 拉取镜像
```shell script
docker pull mysql:8.0.30
```
- 查看镜像
```shell script
docker images
```
- 编写启动脚本
```shell script
docker-mysql.sh
```
```shell script
docker run -d \
  --name mysql-8.0.30 \
  --restart=always \
  --privileged=true \
  -p 3306:3306 \
  -v /opt/env/mysql/conf/my.cnf:/etc/mysql/my.cnf \
  -v /opt/env/mysql/logs:/logs \
  -v /opt/env/mysql/data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  mysql:8.0.30
```
- 添加对应的目录
```shell script
mkdir -p /opt/env/mysql/conf
mkdir -p /opt/env/mysql/logs
mkdir -p /opt/env/mysql/data
chmod -R a+rwx /opt/env/mysql
```
- 添加配置文件
```shell script
vi /opt/env/mysql/conf/my.cnf
```
```shell script
[client]
default-character-set = utf8mb4

[mysqld]
pid-file        = /var/run/mysqld/mysqld.pid
socket          = /var/run/mysqld/mysqld.sock
datadir         = /var/lib/mysql
secure-file-priv= NULL

# Custom config should go here
# 字符集
character_set_server=utf8mb4
collation-server=utf8mb4_unicode_ci
# 创建新表时将使用的默认存储引擎
default-storage-engine=INNODB

symbolic-links=0
explicit_defaults_for_timestamp=true
skip-name-resolve

# 是否对sql语句大小写敏感，0:大小写敏感,1:忽略大小写区别。，只能在初始化服务器时配置。禁止在服务器初始化后更改
# 设置为2时，表名和数据库名按声明存储，但以小写形式进行比较
lower_case_table_names = 1

# 最大连接数
max_connections = 2000
connect_timeout=20000
max_connect_errors=500

# Innodb缓存池大小
innodb_buffer_pool_size = 4G

# 表文件描述符的缓存大小
table_open_cache_instances=2
table_open_cache=2000
table_definition_cache=2000

# 指定执行器数量
innodb_buffer_pool_instances=4


!includedir /etc/mysql/conf.d/
```
- 启动docker镜像
```shell script
./docker-mysql.sh
```
- 查看进程
```shell script
docker -ps
```

