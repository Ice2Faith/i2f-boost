# Agent & Instrumentation

---
## MANIFEST.MF config
```bash
---------------------------------------------
属性	                            说明	                            是否必填	    默认值
Premain-Class	                包含premain方法的类	            依赖启动方式	无
Agent-Class	                    包含agentmain方法的类	            依赖启动方式	无
Boot-Class-Path	                启动类加载器搜索路径	            否	        无
Can-Redefine-Classis	        是否可以重定义代理所需的类	        否	        false
Can-Retransform-Classis	        是否能够重新转换此代理所需的类	    否	        false
Can-Set-Native-Method-Prefix	是否能够设置此代理所需的本机方法前缀	否	        false
```

## run
### premain method,boot agent
- define
```java
public static void premain(String arg, Instrumentation instrumentation){
    ...
}
```
- MANIFEST.MF require
```bash
Premain-Class
```
- boot jvm options
    - [=arg]
    - arg is a string after =
    - is it use on premain/agentmain arg
```bash
-javaagent:D:\agent\test-agent-1.0.jar[=arg]
```
- full command
```bash
java -javaagent:D:\agent\test-agent-1.0.jar -jar main-instance-1.0.jar
```
- example
```bash
-javaagent:D:\i2f-agent-1.0.jar=i2f.*.doHttp*,i2f.*send*
```

### agentmain method,dynamic attach agent
- define
```java
public static void agentmain(String arg, Instrumentation inst){
    ...
}
```
- MANIFEST.MF require
```bash
Agent-Class
```
- boot method
    - by main attach
```java
// get all vm instance
List<VirtualMachineDescriptor> vmds = VirtualMachine.list();

// get attach vm by PID
VirtualMachine vm = VirtualMachine.attach(pid);

// load agent
vm.loadAgent("D:\\agent\\test-agent-1.0.jar","arg1");

// detach
vm.detach();
```

---
### program problem
    - not found VirtualMachine class define
```bash
${JAVA_HOME}/lib/tools.jar
```
- add to libs
- add to classpath

--- 
### maven add manifest items
- simple
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-jar-plugin</artifactId>
    <version>3.1.2</version>
    <configuration>
        <archive>
            <manifestEntries>
                <!--<Premain-Class>com.sim.PremainAgent</Premain-Class>-->
                 <Agent-Class>sim.com.AgentmainAgent</Agent-Class>
                <Can-Redefine-Classes>true</Can-Redefine-Classes>
                <Can-Retransform-Classes>true</Can-Retransform-Classes>
            </manifestEntries>
        </archive>
    </configuration>
</plugin>
```
- with dependency
```xml
 <plugin>
    <artifactId>maven-assembly-plugin</artifactId>
    <version>3.1.1</version>
    <configuration>
        <archive>
            <!--避免MANIFEST.MF被覆盖-->
            <manifestFile>src/main/resources/META-INF/MANIFEST.MF</manifestFile>
        </archive>
        <descriptorRefs>
            <!--打包时加入依赖-->
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
    </configuration>
    <executions>
        <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```
