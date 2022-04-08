## design
- for target object proxy by ValidateProxyHandler
- this handler will check Validate annotation when proxy object be invoke
- support check annotation's annotation
### use case
- so you can use like this
```java
IProxyProvider provider=new JdkProxyProvider();
ITestService service=provider.proxy(new TestServiceImpl(),new ValidateProxyHandler());
service.doSomething(null);
```
- because of jdk proxy use interface(other proxy may be not like this)
- so, Validate annotation should at interfaces rather than implements class
- else, will cause handler not valid work
- use case
```java
public interface ITestService{
    @VNotNull
    String doSomething(@VNotNull @VNotEmpty @Validate(value=true,validators=CustomerValidator.class) String str);
}

public interface TestServiceImpl implements ITestService{
    
    @Override
    public String doSomething(String str){
        return str;
    }
}
```
- when you want validate a java bean , inner support VBean annotation
- but,require this bean not null,
- so you cloud use VNotNull at before
- when argument is null or return value is null,
- both trigger throw exception of runtime,type of ValidateException
```
Exception in thread "main" i2f.core.validate.exception.ValidateException: validate cannot be null
```

### defined your's validate annotations
- why use custom annotation?
    - at this before,write a long Validate annotation is complex
    - but also support that
```java
@Target({
        ElementType.TYPE, // use in class
        ElementType.FIELD, // use in field
        ElementType.PARAMETER // use in parameter
})
@Retention(RetentionPolicy.RUNTIME)
// use annotation's annotation implement short annotation writing
// value form validators return value when equals value will trigger exception
// validates is a validator's array which implements IValidator interface
@Validate(value = true,validators = NullValidator.class)
public @interface VNotNull {

}
```
