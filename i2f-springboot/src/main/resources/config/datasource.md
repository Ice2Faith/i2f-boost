# spring-datasource 配置模板
---
## MySQL
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/test_db?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8
spring.datasource.username=root
spring.datasource.password=root
```
---
## Oracle
```properties
spring.datasource.driver-class-name: oracle.jdbc.driver.OracleDriver
spring.datasource.url: jdbc:oracle:thin:@127.0.0.1:1521:ctmp
spring.datasource.username: root
spring.datasource.password: root
```
---
