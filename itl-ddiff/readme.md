# itl-ddiff

## 简介
- 全称：i2f-tool-dir-diff
- 这个工具是专门为了比对两个文件夹而生的
- 旨在提供不同系统之间比对目录差异
- 方便进行传输差异的文件
- 注意，只提供文件名意义上的差异
- 不提供其他意义上的差异

## 用途
- 用于增量更新
- 比如，最常见的JAVA场景中
- 每次部署更新的时候，可能都需要传输lib包
- 整个lib包可能比较大，网络传输比较耗时
- 因此可以进行比对之后，选择增量传输lib包
- 尽量只传输jar即可，尽量少的传递jar包

## 使用
- 生成目录列表文件
```shell script
java -jar itl-ddiff.jar list [目录] [输出文件]

java -jar itl-ddiff.jar list ./apps server-apps-list.txt
```
- 比较两份目录文件差异
```shell script
java -jar itl-ddiff.jar diff [列表文件1] [列表文件2] [输出文件]

java -jar itl-ddiff.jar diff ./local-apps-list.txt ./server-apps-list.txt ./diff-log.txt
```
- 直接比较并复制差异文件
```shell script
java -jar itl-ddiff.jar copy [目录] [列表文件] [输出目录]

java -jar itl-ddiff.jar copy ./apps ./server-apps-list.txt ./update-apps
```
- 其中会生成两个文件
```shell script
clean-file.bat
clean-file.sh
```
- 是用于删除本地没有但远程有的文件的
- 可选的执行

## 增量更新
- 在服务器上执行list获取到服务器的文件列表文件
```shell script
java -jar itl-ddiff.jar list ./apps server-apps-list.txt
```
- 在本地打包路径直接输出增量更新文件
```shell script
java -jar itl-ddiff.jar copy ./apps ./server-apps-list.txt ./update-apps
```
- 将本地的增量更新文件送到服务器更新
```shell script
./update-apps
```