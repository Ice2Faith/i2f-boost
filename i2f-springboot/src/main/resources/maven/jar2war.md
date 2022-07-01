# springboot 项目 jar 包转 war 包
---

## 修改 pom.xml
- 更改打包方式为 war
```xml
节点：/project

<packaging>war</packaging>
```
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
- 由于去除了tomcat依赖，编译会报错，引入编译时servlet依赖
```xml
节点：/project/dependencies

<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>

```
- 添加maven打包插件
```
节点：/project/build/plugins

<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-war-plugin</artifactId>
    <configuration>
        <failOnMissingWebXml>false</failOnMissingWebXml>
    </configuration>
</plugin>
```
- 修改启动类
    - 继承 SpringBootServletInitializer 类，重写 configure 方法
    - 使得指向启动类
```java
@SpringBootApplication
public class SpringbootApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringbootApplication.class);
    }
}
```
- 刷新maven，重新打包即可
