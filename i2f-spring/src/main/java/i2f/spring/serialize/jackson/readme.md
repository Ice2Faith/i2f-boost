# jackson 序列化&反序列化

## 使用对应的格式，需要导入对应的依赖
- 注意，如果在spring-boot-starter-web环境下
- 不需要指定版本
- 原因是具有如下的依赖关系
- spring-boot-starter-web
    - spring-boot-starter-json
        - jackson-databind
        - jackson-datatype-jdk8
        - jackson-module-parameter-names
            - jackson-core
- 因此，web环境下，也不需要导入JSON的依赖
```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-yaml</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-protobuf</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-cbor</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-csv</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-smile</artifactId>
</dependency>
```
- 非web环境下使用
- 同上，指定版本即可，例如：2.10.3
- json需要的基础依赖
```xml
<dependency>
  <groupId>com.fasterxml.jackson.core</groupId>
  <artifactId>jackson-databind</artifactId>
  <version>2.10.3</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.datatype</groupId>
  <artifactId>jackson-datatype-jdk8</artifactId>
  <version>2.10.3</version>
</dependency>
<dependency>
  <groupId>com.fasterxml.jackson.module</groupId>
  <artifactId>jackson-module-parameter-names</artifactId>
  <version>2.10.3</version>
</dependency>
```

## 用法介绍
- 这里说的用法，大部分是针对注解的设置的
- 因为标准的序列化接口已经定义了，使用也就是一致的了

### 用法总览
```java
import com.fasterxml.jackson.core.type.TypeReference;
import i2f.spring.serialize.jackson.JacksonJsonSerializer;
import i2f.spring.serialize.jackson.JacksonXmlSerializer;
import i2f.spring.serialize.jackson.JacksonYamlSerializer;

import java.util.ArrayList;
import java.util.List;

public class TestJackson {
    public static void main(String[] args){
        testSerializer(new JacksonJsonSerializer());
        testSerializer(new JacksonXmlSerializer());
        testSerializer(new JacksonYamlSerializer());
//        testSerializer(new JacksonProtobufSerializer());
//        testSerializer(new JacksonCborSerializer());
//        testSerializer(new JacksonCsvSerializer());
//        testSerializer(new JacksonSmileSerializer());
    }

    public static<T> void testSerializer(ISerializer<T> serializer){
        System.out.println("==================================================");
        System.out.println("serializer="+serializer.getClass().getSimpleName());
        System.out.println("==================================================");

        UserDto user=UserDto.makeRandom();
        List<UserDto> list=new ArrayList<>();
        list.add(user);
        T xml = serializer.serialize(list,true);
        System.out.println(xml);

        List<UserDto> bean = serializer.deserialize(xml, new TypeReference<List<UserDto>>(){});
        System.out.println(bean);
    }
}
```

### Jackson的反序列化typeToken参数
- Jackson使用的是TypeReference
```java
List<UserDto> bean = serializer.deserialize(xml, new TypeReference<List<UserDto>>(){});
System.out.println(bean);
```
- 注意书写方式
```java
new TypeReference<T>(){}
```
- 第一，类是TypeReference
    - 不要搞错了包，这是jackson包里面的
    - 因为其他的反序列化中，可能也叫这名字
- 第二，这使用无参构造()
- 第三，这是一个匿名内部类，需要{}
    - 匿名内部类，{}一般是实现方法的，但是不需要我们实现方法
    - 这样使用的原因是，java存在泛型擦除
    - 导致反序列化器无法推断正确的类型，全都是Object，类型不明确
    - 因此使用匿名内部类，实现TypeReference
    - 这样，这个匿名内部类，就具有了明确的类型
    - 反序列化器根据这个类型，可以进行类型推断
    - 得到正确的类型

### 针对XML的序列化和反序列化
- XML是需要根节点的
- 使用注解@JacksonXmlRootElement指定
- 字段别名用注解@JacksonXmlProperty指定
- 泛型成员，用@JacksonXmlElementWrapper指定根节点
- 示例如下
```java
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Data
@JacksonXmlRootElement(localName = "root")
public class UserDto {
    private Long userId;
    private String userName;
    private Integer age;
    private Double high;
    private BigDecimal money;
    @JacksonXmlElementWrapper(localName = "roles")
    @JacksonXmlProperty(localName = "item")
    private List<RoleDto> roles;
}
```
- 下面是对应的序列化结果
```xml
<root>
    <userId>2081623805</userId>
    <userName>用户B</userName>
    <age>33</age>
    <high>1.8856344137296204</high>
    <money>2461.689330883734</money>
    <roles>
      <item>
        <roleId>-347486185</roleId>
        <roleKey>rk_G</roleKey>
        <roleName>角色E</roleName>
        <enable>true</enable>
      </item>
    </roles>
</root>
```

### 针对JSON的序列化和反序列化
- JSON本来就是对应了一个bean
- 因此，本身就具有很好的对应bean的方式
- 一般没有特殊的使用方式
- 还是说一下，几个注解
- 反序列化，遇到没有的字段，选择忽略，使用注解@JsonIgnoreProperties
- 需要起别名，使用注解@JsonProperty
- 时间类型，指定序列化格式，使用注解@JsonFormat
```java
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonIgnoreProperties
@Data
public class UserDto {
    @JsonProperty("user_id")
    private Long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date regDate;
}
```

### 针对YAML的序列化和反序列化
- 其实上，YAML是JSON的一种变体
- 因此，使用上和JSON一致
- 不再讲解

### 其他未提及的序列化方式
- 针对protobuf等的序列化方式
- 目前了解不多，实际测试中，也没能正常使用
- 可能是需要额外的设置
- 如果需要，个人自行研究
- 这里不再给出
