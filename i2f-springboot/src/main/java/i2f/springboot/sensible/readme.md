# jackson 数据脱敏
---

## 简介
- 在项目中一般会遇到需要将数据进行脱敏传递给页面
- 例如，手机号，用户真实名称，身份证号，邮箱等
- 这些数据都有一个特点
- 那就是涉及到个人隐私的敏感信息
- 因此，这些信息，在暴露时就需要考虑脱敏

## 数据脱敏
- 既然要实现数据脱敏
- 就需要在传递给前端的时候，将数据变换形式
- 可以直接通过代码方式转换，但是就是代码编写会很多
- 这里就借助jackson提供的字段注解自定义序列化方式来实现
- 通过注解的方式，实现数据写会前端时自动脱敏

## jackson自定义序列化
- 步骤1，继承父类 JsonSerializer<?>
- 覆盖序列化方法
```
void serialize(Object obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
```
- 参数列表：
    - Object obj 要序列化的对象
    - JsonGenerator jsonGenerator 系列化生成器
    - SerializerProvider serializerProvider 序列化器提供器
- 最终需要使用序列化生成器，写入一个对象
```
jsonGenerator.writeObject(obj);
```
- 步骤2，实现接口 ContextualSerializer
- 覆盖上下文序列化器方法
```
JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty)
```
- 参数列表
    - SerializerProvider serializerProvider 序列化器提供器
    - BeanProperty beanProperty 序列化字段或对象的定义
- 这里因为需要使用字段的注解，因此，就需要从上下文接口来返回对应的序列化器
- 也就是，有注解的，返回我们自己定义的序列化器
- 没有注解的，返回默认的序列化器
- 步骤3，在需要使用自定义序列化的字段上，添加注解，使得指向对应的序列化器
```
@JsonSerialize(using=JacksonSensibleSerializer.class)
private String password;
```
- 但是这样的注解比较麻烦
- 配合 @JacksonAnnotationsInside 注解
- 实现使用我们自己的注解的方式
```
@JsonSerialize(using=JacksonSensibleSerializer.class)
@JacksonAnnotationsInside
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Sensible {
    String type() default SensibleType.PHONE;

    int prefix() default 1;

    int suffix() default 1;

    String fill() default "*";

    String param() default "";
}
```

## 如何使用？
- 前面已经介绍完基本的原理了
- 下面就来看目前能够怎么使用
- 步骤1，将本包引入到项目中
    - 注意，JacksonSensibleSerializer 需要能够被扫描到
    - 因为其中会自动从spring容器中读取相关的bean
    - 这个是拓展点，后面讲
- 步骤2，在你需要脱敏的bean的字段上，添加注解 @Sensible
    - 在 SensibleType 中预定义了一些常用的脱敏类型
```
@Data
@NoArgsConstructor
public class TestBean {

    @Sensible(type = SensibleType.PHONE)
    private String phone;

    @Sensible(type = SensibleType.EMAIL)
    private String email;

    @Sensible(type = SensibleType.ID_CARD)
    private String idCard;

    @Sensible(type = SensibleType.PASSWORD)
    private String password;

    @Sensible(type = SensibleType.TRUNC,prefix = 1,suffix = 0,fill = "#")
    private String realname;
}
```
- 步骤3，直接返回给前端即可
- 如果需要手动调试，请查看示例
```
@Component
public class TestSensible implements ApplicationRunner {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        TestBean bean=new TestBean();
        bean.setPhone("18200001111");
        bean.setEmail("554711112222@163.com");
        bean.setIdCard("522000190001010001");
        bean.setPassword("123456");
        bean.setRealname("刘明华");

        String str = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(bean);

        System.out.println(str);
    }
}
```

## 如何拓展？
- 拓展是基于注解 @Sensible 的
- 也就是针对注解的type()属性来说的
```
public @interface Sensible {
    // 脱敏类型
    String type() default SensibleType.PHONE;
    // 保留前缀字符数
    int prefix() default 1;
    // 保留后缀字符数
    int suffix() default 1;
    // 填充字符串
    String fill() default "*";
    // 预留参数位
    String param() default "";
}
```
- 怎么拓展处理？
- 实现脱敏处理接口 ISensibleHandler
- 将实现类加入到spring-bean管理
```
public interface ISensibleHandler {
    Set<String> accept();
    Set<Class<?>> type();
    Object handle(Object obj, Sensible ann);
}
```
- 示例如下
```
public class CustomSensibleHandler implements ISensibleHandler {
    @Override
    public Set<String> accept() {
        // 指定要匹配的注解中的type值有哪些
        return new HashSet<>(Arrays.asList(
                "format","dict"
        ));
    }

    @Override
    public Set<Class<?>> type() {
        return new HashSet<>(Arrays.asList(
            // 指定要处理哪些类型的数据
            String.class
        ));
    }

    @Override
    public Object handle(Object obj, Sensible ann) {
        if(obj==null){
            return obj;
        }
        // 由于前面指定了，只处理String类型的数据，因此此处可强转
        String str=(String)obj;
        String type = ann.type();
        
        // 多类型时，分别匹配转换obj的值
        if("format".equals(type)){
            obj=...;
        } else if("dict".equals(type)){
           obj=...;
       }
        
        return obj;
    }
}
```

## 可能需要的拓展
- 字典翻译 AbsDictSensibleHandler
    - 需要实现抽象方法，注解的param应该指定为字典的取值范围，比如dict_code，dict_class...
- 加密或转码 将指定的字段加密或者转码为另一种形式
