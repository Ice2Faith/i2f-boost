# 安全包
- 提供了安全检查和断言的能力

## 详情
- Asserts
    - 提供基本的检查和断言能力
```java
boolean ok = Asserts.thr()
                .except((e)->new RuntimeException(e))
                .isTrue(false,"isTrue")
                .isFalse(true,"isFalse")
                .isNull(1,"null")
                .isEmpty(" ","empty")
                .isBlank(" 5","blank")
                .notIsArray(new String[]{"a"},"not array")
                .notIsTypeOf(SysUser.class,LoginUser.class,"typeof ok")
                .isTypeOf(LoginUser.class,SysUser.class,"typeof err")
                .notInstanceOf(new LoginUser(),LoginUser.class,"not instance")
                .instanceOf(new SysUser(),LoginUser.class,"instance of")
                .isLt(1,2,"lt")
                .ok();
System.out.println(ok);
```
- Nulls
    - 针对Null的一般处理
- Safes
    - 提供一些安全的方法调用，去除某些java中繁琐的异常处理