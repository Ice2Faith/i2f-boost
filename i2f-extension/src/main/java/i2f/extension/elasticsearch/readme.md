# elasticsearch 工具使用介绍
## 设计理念
- 简化原始API
- 尽量能够链式调用
- 支持最基本的elasticsearch的官方API
- 支持和spring-elasticsearch的封装API
## 使用简介
- 入口类
    - EsManager
    - 用户获取客户端实例，并能够对实例进行管理，一切操作都基于此类
- 查询构建类
    - EsQuery
    - 此类进行构建查询Bean，使用来简化查询
- Bean方式的管理器
    - EsBeanManager
    - 基于基础EsManager之上，构建基于Bean的一个类Jpa管理类
## 简单使用
- 实例化EsManager
    - 提供了静态方法，推荐使用此方式进行实例化
    - 下面是三种使用方式
```java
EsManager manager=EsManager.manager("localhost", 9200);

EsManager manager = EsManager.manager("localhost:9200,192.168.1.52:9200");

EsManager manager = EsManager.manager(new HttpHost("localhost",9200));
```
- 实例化EsBeanManager
    - 提供了静态方法，从EsManager进行实例化
```java
EsBeanManager beanManager = EsManager.manager("localhost", 9200)
                .beanOps();
```
- api使用举例
```java
boolean ok = EsManager.manager("localhost", 9200)
                .indexCreate("idx_test");
```
## 查询
- 原始API，与JSON字符串交互或与Map交互
```java
ApiPage<Map<String, Object>> page = EsManager.manager("localhost", 9200)
                .query()
                .select("name", "password", "age")
                .from("user")
                .where()
                .and()
                .eq("course", "Math")
                .like("teacher", "Ma")
                .likes("class", "v1", "v2")
                .lt("grade", 3)
                .range("number", 60, 100)
                .desc("createTime", "modifyTime")
                .asc("id", "name")
                .page(0, 20)
                .searchAsMap();
```
- 原始API，使用Bean进行交互
```java
ApiPage<Student> page = EsManager.manager("localhost", 9200)
                .beanOps()
                .query()
                .select("name", "password", "age")
                .from("user")
                .where()
                .and()
                .eq("course", "Math")
                .like("teacher", "Ma")
                .likes("class", "v1", "v2")
                .lt("grade", 3)
                .range("number", 60, 100)
                .desc("createTime", "modifyTime")
                .asc("id", "name")
                .page(0, 20)
                .searchAsBean(Student.class);
```
- 基于spring-elasticsearch方式
```java
ApiPage<Student> page = EsManager.manager("localhost", 9200)
                .query()
                .select("name", "password", "age")
                .from("user")
                .where()
                .and()
                .eq("course", "Math")
                .like("teacher", "Ma")
                .likes("class", "v1", "v2")
                .lt("grade", 3)
                .range("number", 60, 100)
                .desc("createTime", "modifyTime")
                .asc("id", "name")
                .page(0, 20)
                .spring()
                .respPage(new ElasticsearchRepository<Student,String>());
```

## EsBeanManager的Bean操作，对Bean需要使用的注解
- EsIndex
    - 用在Bean类上，标志对应的index的name，可以使用alias指定别名
- EsId
    - 用在Bean的字段上，标志这个字段是这个index的id字段
- EsField
    - 用在Bean的字段上，标志这个字段在index中的name,可以使用alias指定别名，可以指定value为false排除这个字段与es交互
- 举例
```java
@EsIndex("idx_student")
public class Student {
    @EsId
    @EsField(alias = "student_id")
    private int id;
    
    private String name;
    
    @EsField(value = false)
    private String keyword;
}
```
- 在这个例子中
- 对应的index=idx_student
- id字段对应es中的student_id字段
- name字段与es中的字段一致
- keyword字段被排除，不再es中表示
