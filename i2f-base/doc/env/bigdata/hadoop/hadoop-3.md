# hadoop3 环境安装

## 简介
- hadoop3 版本虽然向下兼容
- 但是其他的依赖于hadoop3的组件，有的存在版本依赖

## 依赖环境
- java

## 安装
- 下载
```shell script
wget -c https://dlcdn.apache.org/hadoop/common/hadoop-3.2.3/hadoop-3.2.3.tar.gz
```
- 解包
```shell script
tar -zxvf hadoop-3.2.3.tar.gz
```
- 进入
```shell script
cd hadoop-3.2.3
```
- 添加执行权限
```shell script
chmod a+x ./bin/hadoop
```
- 检查版本
```shell script
./bin/hadoop version
```

## 配置集群
