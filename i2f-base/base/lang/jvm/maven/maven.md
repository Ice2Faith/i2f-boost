# maven 依赖管理器

---

## 简介
- maven是作为java项目的一个依赖管理器
- 或者说是包管理器
- 由于其配置简单，上手速度快，易操作
- 插件丰富等特点
- 被广泛使用在java项目中

## 前提
- 需要java环境
- 如果未安装java
- 请先安装jdk

## 安装
- 下载
```shell script
https://maven.apache.org/download.cgi
```
- windows环境
- 下载zip即可
```shell script
wget -c https://dlcdn.apache.org/maven/maven-3/3.9.4/binaries/apache-maven-3.9.4-bin.zip
```
- 后面需要配置环境
- 因此为了方便
- 将创建以下路径
```shell script
mkdir D:\maven
```
- 将下载的zip包放到路径下
- 并解压，得到如下目录结构
```shell script
D:\
    maven
      apache-maven-3.9.4
        bin
        boot
        conf
          settings.xml
          ...
        lib
        ...
```
- 确认目录结构是否一致
- 添加本地仓库目录
```shell script
mkdir D:\maven\mvn
```
- 得到最终目录结构如下
```shell script
D:\
    maven
      apache-maven-3.9.4
        bin
        boot
        conf
          settings.xml
          ...
        lib
        ...
      mvn
```
- 编辑配置
```shell script
vi conf/settings.xml
```
- 修改本地仓库路径
    - 节点：settings/localRepository
- 添加阿里仓库镜像
    - 节点：settings/mirrors
```shell script
<localRepository>D:\maven\mvn</localRepository>

<mirrors>

	<mirror>  
	  <id>alimaven</id>  
	  <name>aliyun maven</name>  
	  <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
	  <mirrorOf>central</mirrorOf>          
	</mirror> 
	<mirror> 
		<id>UK</id> 
		<name>UK Central</name> 
		<url>http://uk.maven.org/maven2</url> 
		<mirrorOf>central</mirrorOf> 
	</mirror> 
	<mirror> 
		<id>ibiblio.org</id>
		<name>ibiblio Mirror of http://repo1.maven.org/maven2</name> 
		<url>http://mirrors.ibiblio.org/pub/mirrors/maven2</url> 
		<mirrorOf>central</mirrorOf> 
	</mirror> 
	<mirror> 
		<id>jboss-public-repository-group</id> 
		<name>JBoss Public Repository Group</name> 
		<url>http://repository.jboss.org/nexus/content/groups/pubic</url> 
		<mirrorOf>central</mirrorOf> 
	</mirror> 
	<mirror> 
		<id>maven.net.cn</id> 
		<name>oneof the central mirrors in china</name> 
		<url>http://maven.net.cn/content/groups/public/</url> 
		<mirrorOf>central</mirrorOf> 
	</mirror>

  </mirrors>
```
- 配置环境变量
```shell script
MAVEN_HOME
D:\maven\apache-maven-3.9.4

Path
%MAVEN_HOME%\bin
```
- 验证
```shell script
mvn -v
```
- 显示配置信息
```shell script
mvn help:system
```

## 在IDEA中使用
- 打开一个maven项目
- 这时候，idea会自动识别maven项目
- 并开始自动导入import
- 找到maven页签
- 或者在setting里面搜索maven
- 配置maven路径
- 覆盖配置文件用我们自己的
- 辅助本地仓库路径，也用我们自己的
- 重新运行import导入包即可
