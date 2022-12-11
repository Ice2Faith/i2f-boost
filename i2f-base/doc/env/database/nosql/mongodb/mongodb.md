# mongodb 入门

## 简介
- mongodb是一个NoSql数据库
- 基于bson存储json文档

## 依赖环境
- 无

---
## 环境搭建
- 下载
```shell script
wget -c https://fastdl.mongodb.org/linux/mongodb-linux-x86_64-rhel80-6.0.3.tgz
```
- 解压:
```shell script
tar -zxvf mongodb-linux-x86_64-rhel80-6.0.3.tgz
mv mongodb-linux-x86_64-rhel80-6.0.3 mongodb-6.0.3
```
- 更改配置文件
```shell script
cd mongodb-6.0.3
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
systemLog:
   #MongoDB发送所有日志输出的目标指定为文件
   # #The path of the log file to which mongod or mongos should send all diagnostic logging information
   destination: file
   #mongod或mongos应向其发送所有诊断日志记录信息的日志文件的路径
   path: "../mongo-datas/logs/mongo.log"
   #当mongos或mongod实例重新启动时，mongos或mongod会将新条目附加到现有日志文件的末尾。
   logAppend: true
storage:
   #mongod实例存储其数据的目录。storage.dbPath设置仅适用于mongod。
   ##The directory where the mongod instance stores its data.Default Value is "/data/db".
   dbPath: "../mongo-datas/data/db"
   journal:
      #启用或禁用持久性日志以确保数据文件保持有效和可恢复。
      enabled: true
processManagement:
   #启用在后台运行mongos或mongod进程的守护进程模式。
   fork: true
net:
   #服务实例绑定的IP，默认是localhost
   bindIp: 0.0.0.0
   #bindIp
   #绑定的端口，默认是27017
   port: 27017
```
- 编写启动文件
```shell script
vi start.sh
```
```shell script
./mongod -f ../conf/mongo.conf
```

## 启动服务
```shell script
cd bin
./start.sh
```

## 验证
- 有信息输出即可
```shell script
./mongod
```



