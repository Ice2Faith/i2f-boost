# mongodb5 入门

## 简介
- mongodb是一个NoSql数据库
- 基于bson存储json文档
- 此文档部门内容，请参考mongodb6的文档

## 依赖环境
- 无

---
## 环境搭建
- 下载
```shell script
wget -c https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel70-5.0.9.tgz
```
- 解压:
```shell script
tar -zxvf mongodb-linux-x86_64-rhel70-5.0.9.tgz
mv mongodb-linux-x86_64-rhel70-5.0.9 mongodb-5.0.9
```
- 更改配置文件
```shell script
cd mongodb-5.0.9
```
- 编辑配置文件
```shell script
mkdir conf
vi ./conf/mongo.conf
```
### 配置
- 主要指定日志目录，数据文件目录，fork守护进程
- 如果启动失败，将fork设置为false查看错误日志
```shell script
dbpath=../mongo-datas/data/db
logpath=../mongo-datas/logs/mongo.log
#以追加的方式记录日志
logappend = true
#端口号 默认为27017
port=27017
#以后台方式运行进程
fork=true
 #开启用户认证
auth=true

#mongodb所绑定的ip地址
bind_ip = 0.0.0.0
#启用日志文件，默认启用
journal=true
#这个选项可以过滤掉一些无用的日志信息，若需要调试使用请设置为false
quiet=true
```
- 编写启动文件
```shell script
cd bin
vi start.sh
```
```shell script
./mongod -f ../conf/mongo.conf
```

## 启动服务
```shell script
./start.sh
```

## 验证
- 有信息输出即可
```shell script
./mongod
```

## 配置认证
- 进入客户端
```shell script
./mongo
```
- 本部分内容除了使用的命令为mongo
- 其余内容同mongodb6部分

## 数据库的dump和restore
- 本章节内容同mongodb6部分