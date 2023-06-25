# file system core

## 简介
- 定义了标准的文件系统接口
- IFile 文件操作接口
- IFileSystem 文件系统接口，主要为提供IFile对象

## JDK
```java
public static void testJdkFs() {
        IFileSystem fs = new JdkFileSystem();

        IFile file = fs.getFile("D:\\01test\\test.txt");

        System.out.println(fs);

        System.out.println(file);

        IFile directory = file.getDirectory();

        System.out.println(directory);

        List<IFile> files = directory.listFiles();

        files.forEach(System.out::println);


    }
```
