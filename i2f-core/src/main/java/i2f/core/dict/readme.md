# dict 字典字段的编码与解码

---

## 应用场景
- 某几个字段在交互过程中，需要进行翻译传递给页面
- 或者需要解码为编码
- 一般就是编码和描述之间的转换
- 也就是 code <--> text 之间的双向转换
- 一般code是一个数值型编码
- 一般text是一个文本描述
- 主要有以下两种
    - 第一种，字典值code和翻译值text公用一个字段
    - 第二种，字典值code和翻译值text分别用不同的字段
- 举例如下
- 公用字段
```java
private String grade;
```
- 分别不同的字段
```java
private String sex;
private String sexDesc;
```
- 下面就以上面这两个字段
- 来讲解对应关系
```shell script
grade
1 === 一级
2 === 二级
3 === 三级

sex
1 === 男
2 === 女
```
- 则根据要求，编写对应的实体类
```java
@Data
@NoArgsConstructor
public class TestBean {

    @DictEncode("sexDesc")
    private Integer sex;

    @DictDecode("sex")
    @Dict(code = "1",text = "男")
    @Dict(code = "2",text = "女")
    @Dict(code = "",text = "未知")
    private String sexDesc;

    @DictDecode
    @DictEncode
    @Dict(code = "1",text = "一级")
    @Dict(code = "2",text = "二级")
    @Dict(code = "3",text = "三级")
    @Dict(code = "",text = "未知")
    private String grade;
}
```
- 使用 @DictEncode 注解，表示需要将text转换为code
- 使用 @DictDecode 注解，表示需要将code转换为text
- 使用 @Dict 注解，指定字典的对应关系
- 对于 sex 而言，sex 字段存储code，sexDesc 字段存储text
    - 则 @DictEncode 标注为编码，其中指定使用sexDesc字段的值来编码，完成将sexDesc的text值转换为sex的code值
    - 由于 sexDesc 或 sex 字段上的 @Dict 注解指定了字典关系
    - 因此按照此字段关系来编码
    - 则 @DictDecode 标注为解码，其中指定使用sex字段的值来解码，完成将sex的code值转换为sexDesc的text值
- 对于 grade 而言，@DictDecode 和 @DictEncode 都作用在他自己身上
    - 因此，code和text都由一个字段存储
- 下面来看调用
```java
public static void main(String[] args) throws Exception {
    TestBean bean=new TestBean();
    bean.setSex(1);
    bean.setGrade("1");

    DictResolver.decode(bean);

    System.out.println(bean);

    bean.setSex(null);
    DictResolver.encode(bean);
    System.out.println(bean);

}
```
- 输出结果如下
```shell script
TestBean(sex=1, sexDesc=男, grade=一级)
TestBean(sex=1, sexDesc=男, grade=1)
```

## 拓展
- DictResolver 的 encode 和 decode 方法具有重载方法
- 支持传入多个 IDictProvider 示例，来解析得到字典表
- 默认使用解析 @Dict 注解的 DictsAnnotationDictProvider 示例
- 在业务中，可能更常用的方式是
- 字典根据某个键值，存放在专门的字典表中
- 这种方式，通过使用注解 @DictMap 注解来替换 @Dict 注解使用
- @DictMap 注解提供了字典的二级分类支持，即group和type
- 只需要继承实现抽象类 AbsDictMapAnnotationDictProvider 
- 根据 @DictMap 注解的字典键，返回对应的字典列表即可