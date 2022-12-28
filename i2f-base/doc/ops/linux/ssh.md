# ssh 免密登录
---

## 简介
- 其实也就是在目标主机的目标用户给定本机的授权key
- 这样就是认证了本机的登录
- 因此本机可以免密登录

## 配置
- 安装openssh
```shell script
yum install -y openssh
```
- 本机创建RSA秘钥
```shell script
ssh-keygen -t rsa
```
- 复制刚创建的秘钥公钥
```shell script
cat ~/.ssh/id_rsa.pub
```
- 在需要免密的目标机上编辑文件
```shell script
vi ~/.ssh/authorized_keys
```
- 将刚才复制的秘钥追加到后面
- 注意如果之前已经有内容了
- 需要先换行，起一个新行

## 使用
- 远程复制
```shell script
scp [目标主机免密用户]@[远程主机]:[远程主机路径] [本机路径]

scp root@192.168.1.100:/root/apps/ /root/apps/app.jar
```