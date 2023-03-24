# 通用maven-java项目分离打包方式

----------------------------------------------------

## 简介
- 这是一个支持单maven项目和多模块maven项目的jar分离打包方式
- 将依赖的jar和自己的源代码分离
- 实现分离成如下结构：依赖、资源/配置、jar
- 一般情况下，如果我们部署或者更新项目的时候
- 依赖和配置是极少修改的
- 尤其依赖包含各种jar包，导致整个jar包比较大
- 不方便传输
- 使用此打包方式
- 你将得到这样标准的tar.gz包
    - 这里假设你的模块叫做：app-svc
```shell script
app-svc-all.tar.gz
    app-svc
        lib
          *.jar
          ...
        resources
          *.yml
          *.properties
          ...
        app-svc.jar
        jarctrl.sh
```
- 因此直接解压启动即可
```shell script
tar -xzvf app-svc-all.tar.gz
cd app-svc
./jarctrl.sh restart
```
- 这样一个项目就完成了打包、部署、运行
- 当我们需要更新的时候
- 同时打出了一个这样的tar.gz包
```shell script
app-svc-upgrade.tar.gz
    app-svc
      lib
        *.jar
        ...
      resources
        *.xml
        ...
      app-svc.jar
```
- 一样的包结构
- 因此，只需要上传、解包覆盖、运行即可
```shell script
tar -xzvf app-svc-upgrade.tar.gz
cd app-svc
./jarctrl.sh restart
```
- 从操作来上来看
- 完整包部署和更新包部署是一样的
- 区别如下
    - lib 目录
        - 完整包包含所有依赖的jar
        - 更新包只包含多模块项目下以来的其他模块的jar
        - 也就是说jar包的数量更少
        - 如果是单项目的话，则没有jar包
    - resources 目录
        - 仅包含常见的可能频繁更新的配置文件或者资源文件
        - 例如mapper目录，一般存放mybatis的xml文件
        - 例如static和public目录，一般存放静态资源
        - 例如templates目录，一般存放模板文件或者jsp文件
    - 剩下的就是jar包，大多数的更新，实际上都是在更新jar包

----------------------------------------------------

## 使用
- 在自己的pom.xml文件中添加app-svc/pom.xml中的节点
- 为了保持节点信息，具体位置参考此pom.xml
- 添加项目属性
```xml
<!-- 父工程重定义通用信息 -->
<properties>
    <java.version>1.8</java.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```
- 添加构建节点
```xml

    <build>
        <!-- 指定资源路径，部分项目的xml写在java路径中，需要包含进来 -->
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                    <include>**/*.json</include>
                    <include>**/*.ftl</include>
                </includes>
            </resource>
        </resources>

        <plugins>
            <!-- 规定java版本等信息，防止每次都要设置jdk版本和编码 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.1</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                    <compilerArgs>
                        <arg>-Xlint:unchecked</arg>
                        <arg>-Xlint:deprecation</arg>
                    </compilerArgs>
                </configuration>
            </plugin>

            <!-- 打包插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <!-- 指定生成的jar包名 -->
                    <finalName>${project.artifactId}</finalName>
                    <archive>
                        <!-- 去除maven描述信息 -->
                        <addMavenDescriptor>false</addMavenDescriptor>
                        <manifest>
                            <!-- 添加classpath,指定classpath前缀，指定启动类 -->
                            <addClasspath>true</addClasspath>
                            <classpathPrefix>lib/</classpathPrefix>
                            <!-- 指定启动类 -->
                            <mainClass>com.i2f.AppSvcApplication</mainClass>
                            <addDefaultImplementationEntries>true</addDefaultImplementationEntries>
                            <addDefaultSpecificationEntries>true</addDefaultSpecificationEntries>
                        </manifest>
                        <manifestEntries>
                            <!-- 添加自己的classpath，多个classpath之间直接空格隔开，写成多个Class-Path标签将被后面的覆盖 -->
                            <Class-Path>. ./resources/</Class-Path>
                            <Timestamp>${maven.build.timestamp}</Timestamp>
                        </manifestEntries>
                    </archive>
                    <excludes>
                        <!-- 打包时排除以下文件，这是实现分离配置文件的第一步 -->
                        <!-- 这样就得到了一个单纯的jar包，不包含配置信息 -->
                        <exclude>bin/**</exclude>
                        <exclude>assembly/**</exclude>
                        <exclude>*assembly*.xml</exclude>

                        <exclude>templates/**</exclude>
                        <exclude>static/**</exclude>
                        <exclude>public/**</exclude>

                        <exclude>i18n/**</exclude>
                        <exclude>META-INF/**</exclude>

                        <exclude>mapper/**</exclude>
                        <exclude>*mapper*/**</exclude>

                        <exclude>*mybatis*/**</exclude>

                        <exclude>*spring*.xml</exclude>

                        <exclude>logback/**</exclude>
                        <exclude>*logback*.xml</exclude>

                        <exclude>*.properties</exclude>
                        <exclude>*.yml</exclude>
                        <exclude>*.yaml</exclude>
                        <exclude>*.conf</exclude>
                        <exclude>*.j2</exclude>
                        <exclude>*.config</exclude>
                        <exclude>*.txt</exclude>
                    </excludes>
                </configuration>
            </plugin>

            <!-- 打包插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>2.4.1</version>
                <!-- 使用多个 execution 分别进行执行打包，以重新指定打包文件名 -->
                <executions>
                    <execution>
                        <id>make-all</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <finalName>${project.artifactId}</finalName>
                            <descriptors>
                                <descriptor>src/main/resources/assembly.xml</descriptor>
                            </descriptors>
                        </configuration>
                    </execution>


                    <execution>
                        <id>make-upgrade</id>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                        <configuration>
                            <finalName>${project.artifactId}</finalName>
                            <descriptors>
                                <descriptor>src/main/resources/assembly-upgrade.xml</descriptor>
                            </descriptors>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>


    </build>
```
- 需要更改的点
- 启动类指定
```xml
<!-- 指定启动类 -->
<mainClass>这里改为你的启动类全限定类名</mainClass>
```
- 如果有system的jar包
- 也就是本地jar，需要在这里添加到classpath
- 其中的xxx1和xxx2就是你的本地jar的文件名称
- 注意，是文件名称，而不是引入定义的坐标
- 另外，你的本地jar需要在以下三个路径之一，否则将不会被打包
    - 其中 ${pom.basedir} 表示pom.xml所在的路径
    - 项目/模块路径下的lib目录：${pom.basedir}/lib
    - 项目/模块的资源目录下的lib目录：${pom.basedir}/src/main/resources/lib
    - 父工程路径下的lib目录：${pom.basedir}/../lib
    - 具体写法参考 app-svc/pom.xml 及响应的文件位置
```xml
<!-- 添加自己的classpath，多个classpath之间直接空格隔开，写成多个Class-Path标签将被后面的覆盖 -->
<Class-Path>. ./resources/ lib/xxx1.jar lib/xxx2.jar</Class-Path>
```
- 拷贝logback文件，启动脚本，编译配置文件到resources目录下
- 也就是resources下的这几个文件（夹）
```shell script
bin
logback
assembly.xml
assembly-upgrade.xml
logback-spring.xml
```
- 现在，你就可以执行 
- 重新引入依赖：maven reimport
- 清空生成：maven clean
- 打包：maven package

## 结束语
- 当然，不同的项目，习惯可能不同
- 如果打出的jar包不适用，或者无法启动
- 则需要对应修改 pom.xml assembly.xml assemble-upgrade.xml 三个文件