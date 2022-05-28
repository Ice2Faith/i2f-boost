# cloud-alibaba 需要的服务端组件

---
## 介绍
- nacos是基于CS模式的，也就是各个微服务都是客户端，向nacos的服务端注册和获取配置信息
- sentinel也是基于CS模式的，但是没有那么强制需要服务端，服务端指示能够更好的进行可视化配置

---
## nacos-server
- 对于nacos而言，提供了.zip包和.tar.gz包
- 实际上，两个包都是需要java环境运行的
- 所以，只要你能解压.tar.gz包，那么在windows环境下也是可以使用的
- 那么为什么还需要两个包呢？
- 原因
- .tar.gz包可以包含权限信息
- 在linux下想要执行一个程序或者脚本，是需要可执行权限的
- 而在Windows下，双击就行了

### 启动
- 直接解压进入bin路径启动即可
- 但是，默认是集群模式的
- 所以，单击使用的话，启动模式需要修改一下
- 进入路径nacos/bin
```
cd nacos/bin
```
- 编辑启动脚本
```
vi starup.sh
```
- 找到如下模式行
```
set MODE="standalone"
```
- 可选的值
```
cluster 集群
standalone 单机
```
- 在脚本后面的判定中可以找到，直接复制就行
- 运行启动脚本即可
- 直到出现如下的日志行，则启动完毕
```
2022-05-28 20:58:52,151 INFO Nacos started successfully in stand alone mode. use embedded storage
```
- 同时就可以进入浏览器访问了
- 日志中有输出访问路径
```
 Console: http://localhost:8848/nacos/index.html
```
- 默认的用户名和密码
```
nacos/nacos
```


### 配置存入数据库
- 默认的配置数据和登录信息存放在内存中
- 因此，如果有需要，可以配置到数据库中
- 配置到数据库中，也才能实现集群
- 进入路径nacos/conf
```
cd nacos/conf
```
- 编辑配置文件
```
vi application.properties
```
- 找对入下行，进行对应的数据库信息配置即可
```
#*************** Config Module Related Configurations ***************#
### If use MySQL as datasource:
# spring.datasource.platform=mysql

### Count of DB:
# db.num=1

### Connect URL of DB:
# db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
# db.user.0=nacos
# db.password.0=nacos
```
- 同时，需要对应的建库建表，这个建表脚本在nacos-server中有给出

### 开启权限管理
- 默认情况下，client访问nacos的配置文件中，是不需要指明用户名和密码的
- 但是可以配置开启，开启之后，client中的配置文件就需要指明用户名和密码
- 找到入下行开启即可
```
#spring.security.enabled=false

nacos.core.auth.enabled=false
```

### 集群部署
- 前面说了改成单机模式的方式
- 对应改成集群的方式就不再给出，同时注意下虚拟机堆大小，默认的配置有点大
- 集群，就需要数据一致，所以就需要数据库模式
- 上面有说到，怎么改为数据库的方式

- 集群配置，主要的就是集群需要知道自己集群有哪些主机
- 所以需要修改配置
-进入配置路径nacos/conf
```
cd nacos/conf
```
- 复制模板配置文件
```
cp cluster.conf.example cluster.conf
```
- 编辑集群配置文件
```
vi cluster.conf
```
- 保存重启即可
- 对于集群的nacos-server来说
- 一般需要一个负载均衡器在前面支撑
- 一般使用nginx
- 配置nginx进行负载均衡
```
http {
   upstream nacos-cluster {
        server 127.0.0.1:8850;
        server 127.0.0.1:8851;   
   }

    server {
        listen  8849;
        server_name localhost;

        location /nacos/{
            proxy_pass http://nacos-cluster/nacos/;
        }
    }

}
```


---
## sentinel
