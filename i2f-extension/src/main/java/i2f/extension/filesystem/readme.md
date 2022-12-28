# file system extension

## 简介
- 拓展了标准的文件系统接口规范
- 实现了FTP、SFTP、MinIO、HDFS四种当前流行的文件系统
- 提供了便捷的切换文件系统的能力

## FTP
```java
public static void testFtpFs() throws Exception {
        FtpMeta meta = new FtpMeta();
        meta.setHost("x.x.x.x");
        meta.setPort(21);
        meta.setUsername("root");
        meta.setPassword("xxx");

        FtpFileSystem fs = new FtpFileSystem(meta, true);
        System.out.println(fs);

        IFile file = fs.getFile("/root/home/test/ftp.txt");
        if (!file.getDirectory().isExists()) {
            file.getDirectory().mkdirs();
        }
        if (!file.isExists()) {
            file.writeText("hello", "UTF-8");
        }

        if (file.isExists()) {
            String str = file.readText("UTF-8");
            System.out.println("read:" + str);
            file.delete();
        }

        if (file.getDirectory().isExists()) {
            file.getDirectory().delete();
        }

        List<IFile> list = file.getDirectory().getDirectory().listFiles();
        list.forEach(System.out::println);

        fs.close();

    }
```

## SFTP
```java
public static void testSftpFs() throws Exception {
        SftpMeta meta = new SftpMeta();
        meta.setHost("x.x.x.x");
        meta.setPort(22);
        meta.setUsername("root");
        meta.setPassword("xxx");

        SftpFileSystem fs = new SftpFileSystem(meta);
        System.out.println(fs);

        IFile file = fs.getFile("/root/home/test/ftp.txt");
        if (!file.getDirectory().isExists()) {
            file.getDirectory().mkdirs();
        }
        if (!file.isExists()) {
            file.writeText("hello", "UTF-8");
        }

        if (file.isExists()) {
            String str = file.readText("UTF-8");
            System.out.println("read:" + str);
            file.delete();
        }

        if (file.getDirectory().isExists()) {
            file.getDirectory().delete();
        }

        List<IFile> list = file.getDirectory().getDirectory().listFiles();
        list.forEach(System.out::println);

        fs.close();

    }
```

## MinIO
```java
public static void testMinioFs() {
        MinioMeta meta = new MinioMeta();
        meta.setUrl("http://x.x.x.x:9000");
        meta.setAccessKey("minioadmin");
        meta.setSecretKey("minioadmin");

        MinioFileSystem fs = new MinioFileSystem(meta);

        IFile file = fs.getFile("/home");

        if (!file.isExists()) {
            file.mkdir();
        }
        System.out.println(file);

        List<IFile> list = file.listFiles();
        list.forEach(System.out::println);

        IFile attachFiles = file.getFile("/home/alarm/files");
        if (!attachFiles.isExists()) {
            attachFiles.mkdirs();
        }
        System.out.println(attachFiles);


    }
```

## HDFS
```java
public static void testHdfs() throws Exception {
        HdfsMeta meta=new HdfsMeta();
        meta.setDefaultFs("hdfs://x.x.x.x:9000");
        meta.setUser("root");
        meta.setUri("hdfs://x.x.x.x:9000");

        HdfsFileSystem fs=new HdfsFileSystem(meta);
        System.out.println(fs);


        IFile file = fs.getFile("/home/test/hdfs.txt");
        if (!file.getDirectory().isExists()) {
            file.getDirectory().mkdirs();
        }
        if (!file.isExists()) {
            file.writeText("hello", "UTF-8");
        }

        if (file.isExists()) {
            String str = file.readText("UTF-8");
            System.out.println("read:" + str);
            file.delete();
        }

        if (file.getDirectory().isExists()) {
            file.getDirectory().delete();
        }

        List<IFile> list = file.getFile("/").listFiles();
        list.forEach(System.out::println);

        fs.close();
    }
```