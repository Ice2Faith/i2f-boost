# springboot 项目替换默认 tomcat 为 undertow
---

## 修改 pom.xml
- 排除web中对的内置tomcat依赖
```xml
节点：/project/dependencies

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <!-- 排除tomcat依赖 -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```
- 添加undertow为默认的web容器
```xml
节点：/project/dependencies

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-undertow</artifactId>
</dependency>
```

- 刷新maven，重新打包即可
