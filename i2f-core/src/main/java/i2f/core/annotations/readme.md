# 注解包
- 用于提供一些功能上或者源码阅读上的帮助

## 详情
- @Nullable
    - 注解用于表示参数或者字段是可以为空的标记
```java
import i2f.core.annotations.notice.Nullable;
public boolean isEmpty(@Nullable String str){
...
}
```
- @NotNull
    - 注解用于表示参数或者字段是不可以为空的标记
```java
import i2f.core.annotations.notice.NotNull;
public int add(@NotNull Integer a,@NotNullInteger b){
...
}
```
- @Name
    - 注解用于在任意位置，表示对应的名称，可用于标记别名
    - 可以用于在避免javac编译之后参数名称丢失，借助注解拿到参数名称的能力
```java

public void doSomething(String className){
...
}
```
- @Comment
    - 注解用于在任意位置，对对应的代码进行注释
```java

public void doSomething(){
...
}
```