package com.i2f.demo.md5;

import i2f.core.container.collection.Collections;
import i2f.core.database.jdbc.core.IJdbcMeta;
import i2f.core.database.jdbc.core.JdbcProvider;
import i2f.core.database.jdbc.core.TransactionManager;
import i2f.core.digest.MessageDigestUtil;
import i2f.core.type.str.Strings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ltb
 * @date 2022/5/21 19:34
 * @desc
 */
public class TestMd5 {
    public static void main(String[] args) throws Exception{

        String str="123456";

//        showMds(str);

        save2db();
    }

    public static void showMds(String str) throws UnsupportedEncodingException {
        byte[] data=str.getBytes("UTF-8");

        String md5= MessageDigestUtil.getMd5(data);
        System.out.println("MD5:"+md5);

        String sha1=MessageDigestUtil.getSha1(data);
        System.out.println("SHA1:"+sha1);

        String sha256=MessageDigestUtil.getSha256(data);
        System.out.println("SHA256:"+sha256);

        String sha384=MessageDigestUtil.getSha384(data);
        System.out.println("SHA384:"+sha384);

        String sha512=MessageDigestUtil.getSha512(data);
        System.out.println("SHA512:"+sha512);
    }

    public static void save2db() throws SQLException, IOException {
        List<String> pass = Collections.arrayList(
                "nacos", "spring", "springboot", "springBoot", "springcloud", "springCloud", "rabbitMq", "rabbitMQ", "zookeeper",
                "redis", "shiro", "security", "auth", "token", "session", "okhttp", "feign", "rabbin", "actuator", "admin", "administrator",
                "anyone", "test1", "test123", "dev", "dev1", "dev123", "test111", "test11", "dev11", "dev111", "admin1", "admin123", "admin111",
                "root1", "root123", "root111", "svc", "web", "svc123", "svc111", "web123", "web111", "rememberme", "rememberMe", "bigdata", "database",
                "linux", "redhat", "centos", "windows", "macos", "aaa", "111", "111111", "11111111", "com", "cn", "http", "tcp", "ip", "udp", "icmp", "arp",
                "mysql", "oracle", "ora", "gbase", "hbase", "hive", "sqlserver", "kafka", "rocket", "alibaba", "wechat", "qq", "xiaomi", "huawei", "sumsung",
                "oppo", "vivo", "mybatis", "ibatis", "springmvc", "springMVC", "jsp", "vue", "element", "dom", "jquery", "servlet", "jsx", "md5", "mD5", "sha",
                "SHA", "rsa", "rSA", "aes", "aES", "except", "exception", "error", "err", "notfound", "notFound", "notFount404", "badRequest", "badrequest",
                "skyboy", "skygirl"
        );

        List<String> next=new ArrayList<>();
        for(String item : pass){
            next.add(Strings.firstUpperCase(item));
        }
        pass.addAll(next);

        JdbcProvider dao=getJdbc();



        for(String item : pass){

            try{
                byte[] data=item.getBytes("UTF-8");

                String md5= MessageDigestUtil.getMd5(data);
                System.out.println("MD5:"+md5);

                String sha1=MessageDigestUtil.getSha1(data);
                System.out.println("SHA1:"+sha1);

                String sha256=MessageDigestUtil.getSha256(data);
                System.out.println("SHA256:"+sha256);

                String sha384=MessageDigestUtil.getSha384(data);
                System.out.println("SHA384:"+sha384);

                String sha512=MessageDigestUtil.getSha512(data);
                System.out.println("SHA512:"+sha512);

                dao.execute("insert into message_digest_mapping(str,md5,sha1,sha256,sha384,sha512) values(?,?,?,?,?,?)",item,md5,sha1,sha256,sha384,sha512);
            }catch(Exception e){
                e.printStackTrace();
            }
        }


        dao.getTransactionManager().close();

    }

    public static JdbcProvider getJdbc() throws SQLException {
        JdbcProvider dao=new JdbcProvider(new TransactionManager(getMeta()));
        return dao;
    }

    public static IJdbcMeta getMeta(){
        return new IJdbcMeta() {
            @Override
            public String getDriver() {
                return "com.mysql.cj.jdbc.Driver";
            }

            @Override
            public String getUrl() {
                return "jdbc:mysql://localhost:3306/hack_db?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8";
            }

            @Override
            public String getUsername() {
                return "root";
            }

            @Override
            public String getPassword() {
                return "96674258";
            }
        };
    }
}
