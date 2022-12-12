# linux 常用命令
---
- 常见的参数
```shell script
--help 查看命令的帮助
-r/-R 递归 recursive
-f 强制 force
-h 人类友好 human
```

---
## 目录
- 列出目录
```shell script
-h 人类可读
-a 所有
-l 详细列表
-S 根据文件大小排序，最大在上
-t 根据最后修改排序，最后在上
```
```shell script
ls -alh
ls -al
ls -a
ll
```
- 自定义日期格式
```shell script
ls -alh --time-style="+%Y-%m-%d %H:%M:%S"
```
- 查询最后一个日志文件
```shell script
ls -at | head -n 1
```
- 查看最后一个日志文件
```shell script
tail -300f `ls -at | head -n 1`
tail -300f logs/`ls -at  logs | head -n 1`
```
- 创建目录
```shell script
mkdir [目录名称]
mkdir apps
```
- 切换目录
```shell script
cd [目录]
cd /root
cd apps

几个特殊的目录
. 当前目录
cd .
.. 上一级目录
cd ..
~ 用户的家目录
cd ~
- 用户上一次访问的目录
cd -
```
- 删除文件/目录
```shell script
rm [文件名]
rm hello.txt

-f 选项，强制删除，不需要确认
rm -f hello.txt

-r 选项，递归删除，用于删除目录
rm -r test

结合使用，删除任意文件/目录
注意，此命令，谨慎使用，否则删除错误，
将导致系统不可用，无法开机，
因此，不建议在 / 目录执行
建议使用时，使用相对路径 . 限定当前目录下
rm -rf ./test
```

---
## 文件
- 创建文件
```shell script
touch [文件名]
touch readme.txt

echo [文件内容] > [文件名]
echo "hello" > hello.txt
```
- 查看文件
```shell script
cat [文件名]
cat hello.txt
```
- 查看文件末尾
```shell script
tail -n [行数] [文件名]
tail -n 50 hello.txt

-f 自动刷新末尾，一般用来看日志
tail -f -n 300 app.log
tail -300f app.log
```
- 查看文件头部
```shell script
head -n [行数] [文件名]
head -n 50 hello.txt
```
- 编辑文件
```shell script
vi [文件名]
vi app.conf

编辑模式按 i/Insert 按键
此模式，编辑器下方会显示 --insert--

预览模式按 esc 按键
预览模式下 输入 :q 退出编辑，前提是没改过文件
预览模式下 输入 :q! 退出编辑，并不保存修改
预览模式下 输入 :wq 保存并退出
预览模式下 输入 / 开头，加上需要查找的内容，进行查找内容
/username
```

---
## 进程
- 查询进程
```shell script
ps -ef
ps -ef | grep java
```
- 强制结束进程
```shell script
kill -9 [PID]
kill -9 555
```
- 进程资源占用
```shell script
top -c
```
- 内存占用
```shell script
free -m
free -h
```
- 不阻塞终端
```shell script
nohup [命令]
nohup java -jar app.jar
```
- 后台执行
```shell script
[命令] &
java -jar app.jar &
```
- 不阻塞后台执行
```shell script
nohup [命令] &
nohup java -jar app.jar &
```
- 标准错误stderr重定向到标准输出stdout
```shell script
2>&1
java -jar app.jar 2>&1
nohup java -jar app.jar 2>&1 &
```
- 输出控制台日志到文件中
```shell script
 > app.log
java -jar app.jar 2>&1 > app.log
nohup java -jar app.jar 2>&1 > app.log &
```
- 将后台启动的进程的PID输出到文件
    - 这种方式，将PID直接输出到文件中，方便存在同名文件时，ps -ef | grep 时出现多个结果，导致进程kill -9误杀
```shell script
echo $! > app.pid
nohup java -jar app.jar 2>&1 > app.log & echo $! > app.pid
```
- 不需要任何日志输出
    - 也就是将日志输出。指向linux的黑洞文件
```shell script
> /dev/null
java -jar app.jar 2>&1 > /dev/null
nohup java -jar app.jar 2>&1 > /dev/null &
```

---
## 查找
- 查找文件
```shell script
find [目录] -name [名称]
find / -name mysql
find /usr -name redis
```
- 查找文件
```shell script
whereis [名称]
whereis mysql
```

---
## 过滤结果
- 排除
```shell script
grep -v [关键字]
grep -v grep
```
- 包含
```shell script
grep  [关键字]
grep java
```
- 忽略大小写
```shell script
grep -i [关键字]
grep -i exception
```
- 显示行号
```shell script
grep -n
```
- 后续N行
```shell script
grep -A [行数]
grep -A 50
```
- 查询异常日志
```shell script
grep -inA 50 exception
tail -300f app.log | grep -inA 50 exception
```
- 分隔输出
```shell script
awk -F [分隔符] [操作]
awk '{print $2}'
awk -F ":" '{print $2}'
```
- 获取目标名称进程PID号
```shell script
ps -ef | grep -v grep | grep [进程名称] | awk '{print $2}'
ps -ef | grep -v grep | grep app.jar | awk '{print $2}'
```

---
## 网络
- 显示端口占用
```shell script
netstat -ano
```
- 查询占用端口状态
```shell script
netstat -lnp | grep [端口]
netstat -lnp | grep 3306
```
- 查询占用端口的进程
```shell script
netstat -lnp | grep [端口] | awk '{print $7}'
netstat -lnp | grep 3306 | awk '{print $7}'
```
- 查询占用端口的PID
```shell script
netstat -lnp | grep [端口] | awk '{print $7}' | awk -F "/" '{print $1}'
netstat -lnp | grep 3306 | awk '{print $7}' | awk -F "/" '{print $1}'
```
- 查询占用端口的进程信息
```shell script
ps -ef | grep -v grep | grep `netstat -lnp | grep [端口] | awk '{print $7}' | awk -F "/" '{print $1}'`
ps -ef | grep -v grep | grep `netstat -lnp | grep 3306 | awk '{print $7}' | awk -F "/" '{print $1}'`
```
- 判断目标网络端口是否开启
```shell script
telnet [IP] [端口]
telnet 192.168.1.123 8080
telnet www.baidu.com 80
```
- 获取本机IP地址
```shell script
ifconfig -a
```
- 检查目标网络连通性
```shell script
ping [IP]
ping 114.114.114.114
ping www.baidu.com
```
- 检查到达目标网络的路由
```shell script
yum install -y  traceroute
traceroute [IP]
traceroute www.baidu.com
```
- 接口可用性调试
```shell script
curl [选项] [地址]
[选项]
-X 指定http请求方式 GET POST PUT DELETE
-H 指定请求头，需要用引号包起来
-d 指定请求参数，也就是请求体，需要用引号包起来
-O 指定响应输出到文件
```
- GET 请求
```shell script
curl www.baidu.com
curl http://www.baidu.com
curl http://www.baidu.com/s?wd=hello
```
- FORM表单请求
```shell script
curl -X POST -d 'username=admin&age=23' http://localhost:8080/api/form
```
- JSON请求
```shell script
curl -X POST -H "content-type: application/json" -d '{"username":"admin","age":23}' http://localhost:8080/api/json
```
- 下载文件
```shell script
curl -o ./baidu-index.html http://www.baidu.com
```

## 服务
- 查看状态
```shell script
systemctl status [服务名]
```
- 停止
```shell script
systemctl stop [服务名]
```
- 启动
```shell script
systemctl start [服务名]
```
- 重启
```shell script
systemctl restart [服务名]
```
- 开机启动
```shell script
systemctl enable [服务名]
```
- 开机不启动
```shell script
systemctl disable [服务名]
```

## 防火墙
- 查看防火墙状态
```shell script
systemctl status firewalld
```
- 停止防火墙
```shell script
systemctl stop firewalld
```
- 启动防火墙
```shell script
systemctl start firewalld
```
- 重启防火墙
```shell script
systemctl restart firewalld
```
- 开机启动
```shell script
systemctl enable firewalld
```
- 开机不启动
```shell script
systemctl disable firewalld
```
- 基本语法
```shell script
firewall-cmd [选项1] [选项2] [...N]
```
- 查看默认区域
```shell script
firewall-cmd --get-default-zone
```
- 查看支持的区域
```shell script
firewall-cmd --get-zones
```
- 查看规则列表
```shell script
firewall-cmd --list-all
```
- 查看所有区域的规则列表
```shell script
firewall-cmd --list-all-zones
```
- 重载配置
```shell script
firewall-cmd --reload
```
- 将服务允许通过
```shell script
firewall-cmd --zone=public --add-service=[服务的名称]

firewall-cmd --zone=public --add-service=http

--premanent 指定永久模式
需要配合重载配置使用

firewall-cmd --zone=public --add-service=http --premanent
firewall-cmd --reload
```
- 将端口允许通过
```shell script
firewall-cmd --zone=public --add-port=端口号/tcp

firewall-cmd --zone=public --add-port=80/tcp

--premanent 指定永久模式
需要配合重载配置使用

firewall-cmd --zone=public --add-port=80/tcp --premanent
firewall-cmd --reload
```

---
## 磁盘
- 查看磁盘挂载
```shell script
df -h
```
- 查看所有，包含特殊设备
```shell script
df -aT
```
- 查询某个路径的磁盘
```shell script
df -h [路径]

df -h /etc
```
- 查看目录大小
```shell script
du -sh [目录]
du -sh
du -sh /usr
```
- 列出所有分区
```shell script
fdisk -l
```
- 磁盘格式化
```shell script
mkfs -t [文件系统格式] [装置名]

mkfs /root/swapfile
mkfs -t ext3 /root/sddisk

查看支持校验的文件系统格式
mkfs[tab][tab]
```
- 磁盘检验
```shell script
fsck -Cf -t [文件系统格式] [装置名]

fsck -Cf -t ext3 /dev/hdc2

查看支持校验的文件系统格式
fsck[tab][tab]
```
- 磁盘挂载
```shell script
mount -t [文件系统格式] [装置名] [挂载点]

mount /mnt/hdc2 /root/disk2
mount -t ext3 /mnt/hdc3 /root/disk3
```
- 磁盘卸载
```shell script
unmount -df [挂载点]

unmount -df /mnt/hdc2
unmount -df /root/disk3
```

---
## 归档
- tar解包(.tar)
```shell script
tar -xvf [tar包名]
tar -xvf app.tar
```
- tar解包指定路径
```shell script
tar -xvf [tar包名] -C [路径]
tar -xvf app.tar -C app-unpack
```
- tar打包
```shell script
tar -cvf [tar包名] [文件列表]
tar -cvf app.tar app-unpack
tar -cvf app.tar com mapper
```
- tgz解包(.tar.gz/.tgz)
```shell script
tar -zxvf [tgz包名]
tar -zxvf app.tar.gz
tar -zxvf app.tgz -C app-unpack
```
- tgz打包
```shell script
tar -zcvf [tgz包名] [文件列表]
tar -zcvf app.tgz app-unpack
tar -zcvf app.tgz com mapper
```
- zip解包
```shell script
unzip [zip包名]
unzip app.zip
```
- zip解包到指定路径
```shell script
unzip [zip包名] -d [目录]
unzip app.zip -d app
```
- zip打包
```shell script
zip [zip包名] [文件列表]
zip app.zip app
zip app.zip com mapper
```

---
## 权限
- 更改权限
```shell script
chmod == change modifier
a+rwx
a 所有人
rwx 读写执行权限
```
```shell script
chmod [权限描述] [目标文件]
chmod 744 app.jar
chmod a+rx app.jar
chmod +rx app.jar
```
- 递归修改
```shell script
chmod -R a+rx /app
```
- 更改所有者
```shell script
chown == change owner
```
```shell script
chown [用户]:[组] 目标文件
chown root:root app.jar
```
- 递归修改
```shell script
chown -R root:root /app
```


## 环境变量
- 配置环境变量
- 以JAVA_HOME为例
- 环境变量，在linux配置中
```shell script
cat /etc/profile
```
- 但是此配置文件，涉及到系统初始化
- 不建议直接修改此文件
- 一般是建议在此目录下建立初始化脚本形式
```shell script
mkdir /etc/profile.d
```
- 建立java的初始化文件
```shell script
vi /etc/profile.d/java.sh
```
- 添加环境配置
```shell script
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.131-11.b12.el7.x86_64
export PATH=$JAVA_HOME/bin:$PATH

export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
- 保存修改即可
- 重新应用配置
```shell script
source /etc/profile
```
- 当然，你也可以直接操作系统配置文件
- 如果确认没问题的话
- [解释]为什么能建立profile.d下面任意脚本
```shell script
for i in /etc/profile.d/*.sh ; do
    if [ -r "$i" ]; then
        if [ "${-#*i}" != "$-" ]; then 
            . "$i"
        else
            . "$i" >/dev/null
        fi
    fi
done
```
- 这是profile文件中的一段脚本
- 可以看到，此脚本扫描了profile.d下面的所有sh脚本进行执行
- 因此可以这样做


## 用户
- 查看所有用户
```shell script
cat /etc/passwd | cut -d : -f 1
```
- 用户信息都存放在这个文件中
```shell script
cat /etc/passwd
```
```shell script
每一行格式
按照:分隔
分别为
用户名:密码:UID(用户ID):GID(组ID):描述:主目录:默认shell

root:x:0:0:root:/root:/bin/bash
ftp:x:14:50:FTP User:/var/ftp:/sbin/nologin
```
- 添加用户
```shell script
useradd [用户名]

useradd ftp

-d 参数指定home目录
-G 参数指定用户组
-c 参数指定注释信息
useradd ftp -d /var/ftp/pub -G ftp -c FTP
```
- 设置用户密码
```shell script
passwd [用户名]

passwd ftp
# 这里输入密码
```
- 切换用户
```shell script
su [用户名]

su ftp
```
- 删除用户
```shell script
userdel [用户名]

userdel ftp

--remove 选项，附加删除用户的home目录

userdel --remove ftp
```
- 查看用户是否登录
```shell script
who
```
- 查看账号是否被锁定
     - 也就是账号后，是否有 !! 在密码之前
```shell script
sudo awk -F: '/[用户名]/ {print $1,$2}' /etc/shadow

sudo awk -F: '/ftp/ {print $1,$2}' /etc/shadow
```
- 锁定账号
```shell script
sudo passwd -l [用户名]

sudo passwd -l ftp
```
- 杀死用户进程
```shell script
sudo pkill -KILL -u [用户名]

sudo pkill -KILL -u ftp
```

## 组
- 添加组
```shell script
groupadd [组名]

groupadd test
```
- 删除组
```shell script
groupdel [组名]

groupdel test
```
- 修改组
```shell script
groupmod -n [旧组名] [新组名]

groupmod -n test dev
```
