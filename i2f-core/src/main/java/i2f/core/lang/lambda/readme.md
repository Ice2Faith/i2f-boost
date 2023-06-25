# lambda 包
- 提供了针对lambda的系列应用
- 特别是针对lambda中用来取函数名，基于getter、setter提取字段名
- lambda表达式对于方法引用时，获取引用方法名称的能力
- 提供了对于mybatis-plus中的LambdaQuery构造器的底层支持

## 详情
- Lambdas
    - 提供了获取lambda表达式的实现方法名，可能的字段名，绑定的字段、方法获取的能力
    - 此能力，基本是针对方法引用而提供的
    - 从而可以将引用方法的方法名或者字段名得到的目的
```java
System.out.println(Lambdas.methodName(Lambdas::toString));
System.out.println(Lambdas.methodName(Lambdas::notifyAll));
System.out.println(Lambdas.fieldName(SysUser::getTelephone));
System.out.println(Lambdas.implBindField(SysUser::getRealName));
System.out.println(Lambdas.implMethod(SysUser::login));
```
- impl
    - 提供了具体实现字段获取的底层支持