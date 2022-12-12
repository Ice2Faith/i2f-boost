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
