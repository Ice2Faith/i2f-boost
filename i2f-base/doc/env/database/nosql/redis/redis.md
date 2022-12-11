# redis 入门

## 简介
- redis是一个高效的缓存中间件

## 依赖环境
- 无

---
## 环境搭建
- 下载，此处省略
- 解压:
```shell script
tar -zxvf redis-6.2.1.tar.gz
```
- 更改配置文件
```shell script
cd redis-6.2.1
```
- 编译安装
```shell script
make && make install
```
- 编辑配置文件
```shell script
vi redis.conf
```
### 配置
- 主要是添加访问密码
```shell script
requirepass 12315
```
- 编写启动文件
```shell script
vi start.sh
```
```shell script
chmod a+x ./src/redis-server
./src/redis-server redis.conf
echo done.
ps -ef | grep -v grep | grep redis
```
- 编写停止脚本
```shell script
vi stop.sh
```
```shell script
PID=`ps -ef | grep -v grep | grep redis | awk '{print $2}'`
echo redis pid=$PID
kill -9 $PID
echo done.
```

## 启动服务
```shell script
./start.sh
```

## 验证
```shell script
./src/redis-cli -a 12315
```
- 输入命令
```shell script
keys *
```
- 有响应则正确
- 退出客户端
```shell script
quit
```



